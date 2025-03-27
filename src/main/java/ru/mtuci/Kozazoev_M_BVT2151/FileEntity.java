package ru.mtuci.Kozazoev_M_BVT2151;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test_file_table")
// Отмечает класс как сущность JPA и задаёт имя таблицы в базе данных
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Lombok-аннотации для автоматической генерации геттеров, сеттеров и конструкторов
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Устанавливает id как первичный ключ с автоинкрементом
    private Long id;

    @Column(name = "file_name", nullable = false)
    // Поле fileName в таблице, не может быть null
    private String fileName;

    private String contentType;
    // Поле для типа содержимого файла (необязательное, пока не используется)

    @Lob
    // Отмечает поле data как Large Object для хранения больших данных (например, файлов)
    private byte[] data;

}
