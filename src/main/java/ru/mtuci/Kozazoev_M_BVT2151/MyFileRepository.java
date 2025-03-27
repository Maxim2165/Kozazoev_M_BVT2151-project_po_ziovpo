    package ru.mtuci.Kozazoev_M_BVT2151;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface MyFileRepository extends JpaRepository<FileEntity, Long> {
        // Наследует базовые методы CRUD (save, findById, delete и т.д.) для FileEntity с ключом Long

        FileEntity findByFileName(String fileName);
        // Метод для поиска сущности FileEntity по имени файла, автоматически реализован Spring Data JPA
    }