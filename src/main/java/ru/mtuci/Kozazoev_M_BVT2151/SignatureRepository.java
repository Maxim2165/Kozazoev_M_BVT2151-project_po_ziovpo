package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Репозиторий для работы с таблицей сигнатур
@Repository
public interface SignatureRepository extends JpaRepository<SignatureEntity, UUID> {
    List<SignatureEntity> findByStatus(String status); // Поиск сигнатур по статусу
    List<SignatureEntity> findByUpdatedAtAfter(LocalDateTime since); // Поиск сигнатур, обновленных после даты
    List<SignatureEntity> findByIdIn(List<UUID> ids); // Поиск сигнатур по списку ID
}