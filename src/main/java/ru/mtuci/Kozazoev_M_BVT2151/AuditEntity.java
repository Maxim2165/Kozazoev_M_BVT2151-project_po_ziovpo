package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

// Сущность для хранения записей аудита изменений сигнатур
@Entity
@Table(name = "audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID auditId; // Уникальный ID записи аудита

    @Column(name = "signature_id", nullable = false)
    private UUID signatureId; // ID сигнатуры, к которой относится запись

    @Column(name = "changed_by")
    private String changedBy; // Кто сделал изменение (null для пользователей)

    @Column(name = "change_type", nullable = false)
    private String changeType; // Тип изменения: ADD, UPDATE, DELETE

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt; // Дата и время изменения

    @Column(name = "fields_changed")
    private String fieldsChanged; // Какие поля были изменены

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now(); // Устанавливаю дату изменения перед сохранением
    }
}