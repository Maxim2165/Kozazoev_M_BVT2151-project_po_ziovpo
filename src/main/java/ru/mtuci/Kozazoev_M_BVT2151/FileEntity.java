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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Устанавливает id как первичный ключ с автоинкрементом
    private Long id;

    @Column(name = "file_name", nullable = false)

    private String fileName;

    private String contentType;


    @Lob

    private byte[] data;

}
