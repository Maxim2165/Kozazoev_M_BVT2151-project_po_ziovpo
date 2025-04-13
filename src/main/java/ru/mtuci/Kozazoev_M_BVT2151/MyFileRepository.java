package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Репозиторий для работы с таблицей файлов
@Repository
public interface MyFileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileName(String fileName); // Метод для поиска файла по имени
}