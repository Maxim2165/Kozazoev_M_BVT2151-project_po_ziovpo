package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SignatureRepository extends JpaRepository<SignatureEntity, UUID> {

    // Поиск всех актуальных сигнатур
    List<SignatureEntity> findByStatus(String status);

    // Поиск сигнатур, обновленных после указанной даты
    List<SignatureEntity> findByUpdatedAtAfter(LocalDateTime since);

    // Поиск сигнатур по списку UUID
    List<SignatureEntity> findByIdIn(List<UUID> ids);
}