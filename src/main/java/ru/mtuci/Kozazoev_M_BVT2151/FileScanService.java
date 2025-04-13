package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// Сервис для сканирования файлов на наличие сигнатур
@Service
public class FileScanService {
    private final SignatureRepository signatureRepository;
    private static final int CHUNK_SIZE = 8192; // Размер чанка для чтения файла (8192 байт)

    public FileScanService(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository; // Внедряю репозиторий сигнатур
    }

    // Метод для сканирования файла
    public List<SignatureScanResult> scanFile(MultipartFile file) {
        Set<SignatureScanResult> resultSet = new HashSet<>(); // Храню результаты сканирования
        File tempFile = null;
        try {
            System.out.println("Получен файл: " + file.getOriginalFilename() + ", размер: " + file.getSize());
            tempFile = File.createTempFile("scan_", ".tmp"); // Создаю временный файл
            file.transferTo(tempFile); // Сохраняю загруженный файл во временный
            System.out.println("Файл сохранен во временный файл: " + tempFile.getAbsolutePath());

            List<SignatureEntity> signatures = signatureRepository.findByStatus("ACTUAL"); // Загружаю актуальные сигнатуры
            if (signatures.isEmpty()) {
                System.out.println("Актуальные сигнатуры не найдены в базе");
                return new ArrayList<>();
            }
            System.out.println("Загружено сигнатур: " + signatures.size());

            // Вычисляю хэши для firstBytes всех сигнатур
            Map<String, Long> signatureHashes = new HashMap<>();
            for (SignatureEntity signature : signatures) {
                long hash = RabinKarpUtils.calculateHash(signature.getFirstBytes());
                if (hash != -1) {
                    signatureHashes.put(signature.getFirstBytes(), hash);
                }
            }

            // Читаю файл чанками
            try (RandomAccessFile raf = new RandomAccessFile(tempFile, "r")) {
                byte[] chunk = new byte[CHUNK_SIZE];
                int bytesRead;
                long filePosition = 0;
                while ((bytesRead = raf.read(chunk, 0, CHUNK_SIZE)) != -1) {
                    System.out.println("Чтение чанка, позиция: " + filePosition + ", размер: " + bytesRead);
                    if (bytesRead < 8) {
                        System.out.println("Чанк слишком мал для проверки (< 8 байт), пропускаем");
                        continue;
                    }

                    // Ищу совпадения firstBytes в чанке
                    List<Integer> matches = RabinKarpUtils.findMatches(chunk, signatureHashes, signatures);

                    // Группирую сигнатуры по хэшам
                    Map<Long, List<SignatureEntity>> hashToSignatures = new HashMap<>();
                    for (SignatureEntity signature : signatures) {
                        long hash = signatureHashes.get(signature.getFirstBytes());
                        hashToSignatures.computeIfAbsent(hash, k -> new ArrayList<>()).add(signature);
                    }

                    // Проверяю каждое совпадение
                    for (int matchPos : matches) {
                        long globalPos = filePosition + matchPos;
                        long rollingHash = 0;
                        for (int i = 0; i < 8; i++) {
                            rollingHash = (rollingHash * RabinKarpUtils.BASE + (chunk[matchPos + i] & 0xFF)) % RabinKarpUtils.MOD;
                        }
                        List<SignatureEntity> matchedSignatures = hashToSignatures.getOrDefault(rollingHash, Collections.emptyList());
                        for (SignatureEntity signature : matchedSignatures) {
                            if (verifySignature(chunk, matchPos, bytesRead, globalPos, signature, raf)) {
                                // Если сигнатура найдена, добавляю результат
                                SignatureScanResult result = new SignatureScanResult(
                                        signature.getId(),
                                        signature.getThreatName(),
                                        (int) globalPos,
                                        (int) signature.getOffsetEnd(),
                                        true
                                );
                                resultSet.add(result);
                                System.out.println("Сигнатура найдена: " + signature.getThreatName() + ", позиция: " + globalPos);
                            }
                        }
                    }

                    filePosition += bytesRead;
                    if (filePosition < raf.length()) {
                        raf.seek(filePosition - 7); // Делаю шаг назад на 7 байт, чтобы не пропустить сигнатуры на границе чанков
                        filePosition -= 7;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке файла: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        } finally {
            // Удаляю временный файл
            if (tempFile != null && tempFile.exists()) {
                if (tempFile.delete()) {
                    System.out.println("Временный файл удален: " + tempFile.getAbsolutePath());
                } else {
                    System.out.println("Не удалось удалить временный файл: " + tempFile.getAbsolutePath());
                }
            }
        }
        List<SignatureScanResult> results = new ArrayList<>(resultSet);
        System.out.println("Сканирование завершено, найдено совпадений: " + results.size());
        return results;
    }

    // Проверяю, соответствует ли найденная сигнатура всем условиям
    private boolean verifySignature(byte[] chunk, int matchPos, int bytesRead, long globalPos,
                                    SignatureEntity signature, RandomAccessFile raf) {
        if (globalPos < signature.getOffsetStart()) {
            System.out.println("Сигнатура '" + signature.getThreatName() +
                    "' начинается раньше offsetStart, ожидалось: " + signature.getOffsetStart() +
                    ", найдено: " + globalPos);
            return false;
        }

        // Проверяю побайтовое совпадение firstBytes
        byte[] signatureBytes = signature.getFirstBytes().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < 8; i++) {
            if (matchPos + i >= bytesRead || (chunk[matchPos + i] & 0xFF) != (signatureBytes[i] & 0xFF)) {
                System.out.println("Сигнатура '" + signature.getThreatName() +
                        "' не совпала по firstBytes на позиции: " + globalPos);
                return false;
            }
        }

        // Читаю remainder и вычисляю его хэш
        int remainderLength = signature.getRemainderLength();
        byte[] remainderBytes = new byte[remainderLength];
        long remainderStart = globalPos + 8;
        try {
            raf.seek(remainderStart);
            int bytesToRead = remainderLength;
            int bytesReadTotal = 0;
            while (bytesToRead > 0 && bytesReadTotal < remainderLength) {
                int read = raf.read(remainderBytes, bytesReadTotal, bytesToRead);
                if (read == -1) {
                    break;
                }
                bytesReadTotal += read;
                bytesToRead -= read;
            }
            if (bytesReadTotal != remainderLength) {
                System.out.println("Не удалось прочитать remainder для сигнатуры '" +
                        signature.getThreatName() + "', прочитано: " + bytesReadTotal +
                        ", ожидалось: " + remainderLength);
                return false;
            }

            String calculatedHash = calculateSHA256(remainderBytes);
            if (!calculatedHash.equals(signature.getRemainderHash())) {
                System.out.println("Сигнатура '" + signature.getThreatName() +
                        "' не совпала по remainderHash, ожидалось: " + signature.getRemainderHash() +
                        ", найдено: " + calculatedHash);
                return false;
            }

            // Проверяю, что сигнатура не превышает offsetEnd
            long expectedEnd = globalPos + 8 + remainderLength;
            if (expectedEnd > signature.getOffsetEnd()) {
                System.out.println("Сигнатура '" + signature.getThreatName() +
                        "' превышает offsetEnd, ожидалось не больше: " + signature.getOffsetEnd() +
                        ", найдено: " + expectedEnd);
                return false;
            }
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка при чтении remainder для сигнатуры '" +
                    signature.getThreatName() + "': " + e.getMessage());
            return false;
        }
    }

    // Вычисляю SHA-256 хэш для массива байтов
    private String calculateSHA256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Ошибка вычисления SHA-256: " + e.getMessage());
            throw new RuntimeException("Ошибка вычисления SHA-256", e);
        }
    }
}