package ru.mtuci.Kozazoev_M_BVT2151;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private UUID auditId; // Уникальный идентификатор записи аудита

    @Column(name = "signature_id", nullable = false)
    private UUID signatureId; // Ссылка на сигнатуру

    @Column(name = "changed_by")
    private String changedBy; // null - пользователи

    @Column(name = "change_type", nullable = false)
    private String changeType; // Тип изменения: ADD, UPDATE, DELETE

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt; // Дата и время изменения

    @Column(name = "fields_changed")
    private String fieldsChanged; // Измененные поля

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now(); // дата изменения
    }
}