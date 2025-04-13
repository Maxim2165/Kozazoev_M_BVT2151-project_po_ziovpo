package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

// Репозиторий для работы с таблицей аудита
@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, UUID> {
    // Наследую базовые методы для работы с AuditEntity (сохранение, удаление, поиск)
}