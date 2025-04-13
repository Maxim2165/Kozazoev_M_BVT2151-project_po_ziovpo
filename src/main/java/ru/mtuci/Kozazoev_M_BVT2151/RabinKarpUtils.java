package ru.mtuci.Kozazoev_M_BVT2151;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Утилита для поиска сигнатур с помощью алгоритма Рабина-Карпа
public class RabinKarpUtils {
    public static final int WINDOW_SIZE = 8; // Размер окна для поиска (8 байт)
    public static final long BASE = 256; // База для хэширования
    public static final long MOD = 1_000_000_007; // Модуль для хэширования
    private static final long HIGHEST_POWER = calculateHighestPower(); // Предвычисленная степень для скользящего хэша

    // Вычисляю наивысшую степень BASE для скользящего хэша
    private static long calculateHighestPower() {
        long result = 1;
        for (int i = 0; i < WINDOW_SIZE - 1; i++) {
            result = (result * BASE) % MOD;
        }
        return result;
    }

    // Вычисляю хэш для строки firstBytes
    public static long calculateHash(String firstBytes) {
        byte[] bytes = firstBytes.getBytes(StandardCharsets.UTF_8);
        if (bytes.length != WINDOW_SIZE) {
            System.out.println("Ошибка: firstBytes должен быть длиной " + WINDOW_SIZE + " байт, получено: " + bytes.length);
            return -1;
        }
        long hash = 0;
        for (byte b : bytes) {
            hash = (hash * BASE + (b & 0xFF)) % MOD;
        }
        System.out.println("Вычислен хэш для firstBytes '" + firstBytes + "': " + hash);
        return hash;
    }

    // Ищу совпадения firstBytes в данных
    public static List<Integer> findMatches(byte[] data, Map<String, Long> signatureHashes, List<SignatureEntity> signatures) {
        List<Integer> matches = new ArrayList<>();
        if (data.length < WINDOW_SIZE) {
            System.out.println("Чанк слишком мал для поиска, размер: " + data.length);
            return matches;
        }

        // Группирую сигнатуры по хэшам
        Map<Long, List<SignatureEntity>> hashToSignatures = new HashMap<>();
        for (SignatureEntity signature : signatures) {
            long hash = signatureHashes.get(signature.getFirstBytes());
            hashToSignatures.computeIfAbsent(hash, k -> new ArrayList<>()).add(signature);
        }

        // Вычисляю хэш для первого окна
        long rollingHash = 0;
        for (int i = 0; i < WINDOW_SIZE; i++) {
            rollingHash = (rollingHash * BASE + (data[i] & 0xFF)) % MOD;
        }
        checkWindow(data, 0, rollingHash, hashToSignatures, matches);

        // Скользящий хэш для остальных окон
        for (int i = WINDOW_SIZE; i < data.length; i++) {
            rollingHash = (rollingHash + MOD - ((data[i - WINDOW_SIZE] & 0xFF) * HIGHEST_POWER) % MOD) % MOD;
            rollingHash = (rollingHash * BASE + (data[i] & 0xFF)) % MOD;
            checkWindow(data, i - WINDOW_SIZE + 1, rollingHash, hashToSignatures, matches);
        }
        System.out.println("Найдено совпадений firstBytes: " + matches.size());
        return matches;
    }

    // Проверяю, совпадает ли окно с какой-либо сигнатурой
    private static void checkWindow(byte[] data, int start, long rollingHash,
                                    Map<Long, List<SignatureEntity>> hashToSignatures,
                                    List<Integer> matches) {
        if (hashToSignatures.containsKey(rollingHash)) {
            for (SignatureEntity signature : hashToSignatures.get(rollingHash)) {
                byte[] signatureBytes = signature.getFirstBytes().getBytes(StandardCharsets.UTF_8);
                if (signatureBytes.length != WINDOW_SIZE) {
                    System.out.println("Ошибка: firstBytes сигнатуры '" + signature.getThreatName() +
                            "' не равен " + WINDOW_SIZE + " байт");
                    continue;
                }
                boolean isMatch = true;
                for (int j = 0; j < WINDOW_SIZE; j++) {
                    if (start + j >= data.length || (data[start + j] & 0xFF) != (signatureBytes[j] & 0xFF)) {
                        System.out.println("Несовпадение на позиции " + (start + j) + ": ожидалось " +
                                (signatureBytes[j] & 0xFF) + ", найдено " + (data[start + j] & 0xFF));
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    System.out.println("Совпадение firstBytes для сигнатуры '" + signature.getThreatName() +
                            "' на позиции: " + start);
                    matches.add(start);
                }
            }
        }
    }
}