package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private UUID historyId; // Уникальный идентификатор записи истории

    @Column(name = "signature_id", nullable = false)
    private UUID signatureId; // Ссылка на основную сигнатуру

    @Column(name = "version_created_at", nullable = false)
    private LocalDateTime versionCreatedAt; // Дата создания этой версии

    @Column(name = "threat_name", nullable = false)
    private String threatName;

    @Column(name = "first_bytes", length = 8, nullable = false)
    private String firstBytes;

    @Column(name = "remainder_hash", nullable = false)
    private String remainderHash;

    @Column(name = "remainder_length", nullable = false)
    private int remainderLength;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "offset_start")
    private int offsetStart;

    @Column(name = "offset_end")
    private int offsetEnd;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.versionCreatedAt = LocalDateTime.now(); // Устанавливаем дату создания версии
    }
}