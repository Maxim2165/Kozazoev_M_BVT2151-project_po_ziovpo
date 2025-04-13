package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

// Сущность для хранения истории изменений сигнатур
@Entity
@Table(name = "signature_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignatureHistoryEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID historyId; // Уникальный ID записи истории

    @Column(name = "signature_id", nullable = false)
    private UUID signatureId; // ID сигнатуры

    @Column(name = "version_created_at", nullable = false)
    private LocalDateTime versionCreatedAt; // Дата создания этой версии

    @Column(name = "threat_name", nullable = false)
    private String threatName; // Название угрозы

    @Column(name = "first_bytes", length = 8, nullable = false)
    private String firstBytes; // Первые 8 байт

    @Column(name = "remainder_hash", nullable = false)
    private String remainderHash; // Хэш остатка

    @Column(name = "remainder_length", nullable = false)
    private int remainderLength; // Длина остатка

    @Column(name = "file_type")
    private String fileType; // Тип файла

    @Column(name = "offset_start")
    private int offsetStart; // Смещение начала

    @Column(name = "offset_end")
    private int offsetEnd; // Смещение конца

    @Column(name = "status", nullable = false)
    private String status; // Статус сигнатуры

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Дата обновления сигнатуры

    @PrePersist
    protected void onCreate() {
        this.versionCreatedAt = LocalDateTime.now(); // Устанавливаю дату создания версии
    }
}