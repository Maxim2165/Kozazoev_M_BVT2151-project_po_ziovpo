package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

// Репозиторий для работы с таблицей истории сигнатур
@Repository
public interface SignatureHistoryRepository extends JpaRepository<SignatureHistoryEntity, UUID> {
    // Наследую базовые методы для работы с SignatureHistoryEntity
}