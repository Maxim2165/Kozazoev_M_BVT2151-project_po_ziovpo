package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Сущность для хранения файлов в базе данных
@Entity
@Table(name = "test_file_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный ID файла с автоинкрементом

    @Column(name = "file_name", nullable = false)
    private String fileName; // Имя файла

    private String contentType; // Тип содержимого файла (например, "text/plain")

    @Lob
    private byte[] data; // Данные файла в виде массива байтов
}