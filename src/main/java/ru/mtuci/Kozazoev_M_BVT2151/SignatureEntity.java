package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private UUID id; // GUID уникальный идентификатор

    @Column(name = "threat_name", nullable = false)
    private String threatName; // Название угрозы

    @Column(name = "first_bytes", length = 8, nullable = false)
    private String firstBytes; // Первые 8 байт сигнатуры

    @Column(name = "remainder_hash", nullable = false)
    private String remainderHash; // Хэш "хвоста"

    @Column(name = "remainder_length", nullable = false)
    private int remainderLength; // Количество байт в "хвосте"

    @Column(name = "file_type")
    private String fileType; // Тип файла

    @Column(name = "offset_start")
    private int offsetStart; // Смещение начала сигнатуры

    @Column(name = "offset_end")
    private int offsetEnd; // Смещение конца сигнатуры


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Дата и время последнего обновления

    @Column(name = "status", nullable = false)
    private String status; // Статус записи (ACTUAL, DELETED, CORRUPTED)

    @PrePersist
    protected void onCreate() {
        // Автоматически устанавливает дату создания и статус ACTUAL при добавлении записи
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "ACTUAL";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Обновляет дату при изменении записи
        this.updatedAt = LocalDateTime.now();
    }
}