package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

// Сущность для хранения сигнатур в базе данных
@Entity
@Table(name = "signatures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignatureEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id; // Уникальный ID сигнатуры

    @Column(name = "threat_name", nullable = false)
    private String threatName; // Название угрозы

    @Column(name = "first_bytes", length = 8, nullable = false)
    private String firstBytes; // Первые 8 байт сигнатуры

    @Column(name = "remainder_hash", nullable = false)
    private String remainderHash; // Хэш остатка сигнатуры

    @Column(name = "remainder_length", nullable = false)
    private int remainderLength; // Длина остатка сигнатуры

    @Column(name = "file_type")
    private String fileType; // Тип файла

    @Column(name = "offset_start")
    private int offsetStart; // Смещение начала сигнатуры

    @Column(name = "offset_end")
    private int offsetEnd; // Смещение конца сигнатуры

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Дата последнего обновления

    @Column(name = "status", nullable = false)
    private String status; // Статус сигнатуры (ACTUAL, DELETED, CORRUPTED)

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now(); // Устанавливаю дату создания
        if (this.status == null) {
            this.status = "ACTUAL"; // Устанавливаю статус ACTUAL при создании
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now(); // Обновляю дату при изменении
    }
}