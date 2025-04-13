package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

// Контроллер для работы с файлами (загрузка и скачивание)
@RestController
@RequestMapping("/files")
public class MyFileController {
    private final MyFileRepository fileRepository;
    private final FileScanService fileScanService;

    public MyFileController(MyFileRepository fileRepository, FileScanService fileScanService) {
        this.fileRepository = fileRepository; // Внедряю репозиторий файлов
        this.fileScanService = fileScanService; // Внедряю сервис сканирования
    }

    // Эндпоинт для загрузки и сканирования файла
    @PostMapping("/upload")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("Ошибка: Загружен пустой файл");
            return ResponseEntity.badRequest().body("Empty file");
        }
        try {
            System.out.println("Начало сканирования файла: " + file.getOriginalFilename());
            List<SignatureScanResult> results = fileScanService.scanFile(file); // Сканирую файл
            System.out.println("Сканирование завершено, найдено совпадений: " + results.size());
            return ResponseEntity.ok(results); // Возвращаю результаты сканирования
        } catch (Exception ex) {
            System.out.println("Ошибка при сканировании файла: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при сканировании: " + ex.getMessage());
        }
    }

    // Эндпоинт для скачивания файла по ID
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "id") Long id) {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(id);
        if (optionalFileEntity.isEmpty()) {
            System.out.println("Файл с ID " + id + " не найден");
            return ResponseEntity.notFound().build();
        }
        FileEntity fileEntity = optionalFileEntity.get();
        System.out.println("Скачивание файла: " + fileEntity.getFileName());
        return new ResponseEntity<>(fileEntity.getData(), HttpStatus.OK); // Возвращаю данные файла
    }
}