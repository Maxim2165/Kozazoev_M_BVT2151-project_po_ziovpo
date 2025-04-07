package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/files")
// Отмечает класс как REST-контроллер с базовым путём /files
public class MyFileController {
    private final MyFileRepository fileRepository;

    public MyFileController(MyFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) {
        // Обработчик POST-запроса /files/upload для загрузки файла
        if (file.isEmpty()) {
            // Проверяет, пустой ли файл
            return ResponseEntity.badRequest().body("Empty file");
        }

        try {
            String fileName = file.getOriginalFilename();
            // Получает оригинальное имя файла
            byte[] bytes = file.getBytes();
            // Читает содержимое файла в массив байтов

            FileEntity fileEntity = new FileEntity();
            // Создаёт новую сущность FileEntity
            fileEntity.setFileName(fileName);
            fileEntity.setData(bytes);
            // Устанавливает имя и данные файла

            fileRepository.save(fileEntity);
            // Сохраняет сущность в базу данных

            return ResponseEntity.ok().body("Файл успешно загружен: " + fileName);
        } catch (IOException ex) {
            // Обрабатывает исключение при чтении файла
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "id") Long id) {
        // Обработчик GET-запроса /files/download/{id} для скачивания файла по ID
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(id);
        if (optionalFileEntity.isEmpty()) {
            // Ищет файл в базе данных по ID
            return ResponseEntity.notFound().build();
        }

        FileEntity fileEntity = optionalFileEntity.get();
        // Получает сущность файла

        return new ResponseEntity<>(fileEntity.getData(), HttpStatus.OK);
        // Возвращает содержимое файла с кодом 200
    }
}


