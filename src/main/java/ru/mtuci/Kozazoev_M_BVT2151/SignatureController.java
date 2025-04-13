package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Контроллер для управления сигнатурами
@RestController
@RequestMapping("/signatures")
public class SignatureController {
    private final SignatureRepository signatureRepository;
    private final SignatureHistoryRepository historyRepository;
    private final AuditRepository auditRepository;

    public SignatureController(SignatureRepository signatureRepository,
                               SignatureHistoryRepository historyRepository,
                               AuditRepository auditRepository) {
        this.signatureRepository = signatureRepository; // Внедряю репозиторий сигнатур
        this.historyRepository = historyRepository; // Внедряю репозиторий истории
        this.auditRepository = auditRepository; // Внедряю репозиторий аудита
    }

    // Эндпоинт для добавления новой сигнатуры
    @PostMapping("/add")
    public ResponseEntity<SignatureEntity> addSignature(@RequestBody SignatureRequest signatureRequest) {
        SignatureEntity signature = new SignatureEntity();
        signature.setThreatName(signatureRequest.getThreatName());
        signature.setFirstBytes(signatureRequest.getFirstBytes());
        signature.setRemainderLength(signatureRequest.getRemainderLength());
        signature.setFileType(signatureRequest.getFileType());
        signature.setOffsetStart(signatureRequest.getOffsetStart());
        signature.setOffsetEnd(signatureRequest.getOffsetEnd());

        // Вычисляю хэш для remainder, если он есть
        String remainder = signatureRequest.getRemainder();
        if (remainder != null && !remainder.isEmpty()) {
            String remainderHash = SignatureUtils.calculateSHA256(remainder);
            signature.setRemainderHash(remainderHash);
        } else {
            signature.setRemainderHash(null);
        }

        SignatureEntity savedSignature = signatureRepository.save(signature); // Сохраняю сигнатуру

        // Логирую добавление в аудит
        AuditEntity audit = new AuditEntity();
        audit.setSignatureId(savedSignature.getId());
        audit.setChangedBy(null);
        audit.setChangeType("ADD");
        audit.setFieldsChanged("all");
        auditRepository.save(audit);

        return ResponseEntity.ok(savedSignature);
    }

    // Эндпоинт для получения всех актуальных сигнатур
    @GetMapping("/all")
    public ResponseEntity<List<SignatureEntity>> getAllSignatures() {
        List<SignatureEntity> signatures = signatureRepository.findByStatus("ACTUAL");
        return ResponseEntity.ok(signatures);
    }

    // Эндпоинт для получения сигнатур, обновленных после указанной даты
    @GetMapping("/diff")
    public ResponseEntity<?> getDiff(@RequestParam("since") String since) {
        try {
            LocalDateTime sinceDate = LocalDateTime.parse(since);
            List<SignatureEntity> signatures = signatureRepository.findByUpdatedAtAfter(sinceDate);
            return ResponseEntity.ok(signatures);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Неверный формат даты. Используйте ISO-формат, например, '2025-03-31T20:00:00'");
        }
    }

    // Эндпоинт для получения сигнатур по списку ID
    @PostMapping("/by-ids")
    public ResponseEntity<List<SignatureEntity>> getSignaturesByIds(@RequestBody SignatureIdsRequest request) {
        if (request.getIds() == null || request.getIds().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<SignatureEntity> signatures = signatureRepository.findByIdIn(request.getIds());
        return ResponseEntity.ok(signatures);
    }

    // Эндпоинт для удаления сигнатуры (меняю статус на DELETED)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSignature(@PathVariable("id") UUID id) {
        SignatureEntity signature = signatureRepository.findById(id).orElse(null);
        if (signature == null) {
            return ResponseEntity.notFound().build();
        }

        // Сохраняю старую версию в историю
        SignatureHistoryEntity history = new SignatureHistoryEntity();
        history.setSignatureId(signature.getId());
        history.setThreatName(signature.getThreatName());
        history.setFirstBytes(signature.getFirstBytes());
        history.setRemainderHash(signature.getRemainderHash());
        history.setRemainderLength(signature.getRemainderLength());
        history.setFileType(signature.getFileType());
        history.setOffsetStart(signature.getOffsetStart());
        history.setOffsetEnd(signature.getOffsetEnd());
        history.setStatus(signature.getStatus());
        history.setUpdatedAt(signature.getUpdatedAt());
        historyRepository.save(history);

        // Меняю статус на DELETED и сохраняю
        signature.setStatus("DELETED");
        signatureRepository.save(signature);

        // Логирую удаление в аудит
        AuditEntity audit = new AuditEntity();
        audit.setSignatureId(id);
        audit.setChangedBy(null);
        audit.setChangeType("DELETE");
        audit.setFieldsChanged("status");
        auditRepository.save(audit);

        return ResponseEntity.ok("Сигнатура с ID " + id + " отмечена как УДАЛЕНА");
    }

    // Эндпоинт для обновления сигнатуры
    @PutMapping("/{id}/update")
    public ResponseEntity<SignatureEntity> updateSignature(@PathVariable("id") UUID id, @RequestBody SignatureRequest signatureRequest) {
        SignatureEntity signature = signatureRepository.findById(id).orElse(null);
        if (signature == null) {
            return ResponseEntity.notFound().build();
        }

        // Сохраняю старую версию в историю
        SignatureHistoryEntity history = new SignatureHistoryEntity();
        history.setSignatureId(signature.getId());
        history.setThreatName(signature.getThreatName());
        history.setFirstBytes(signature.getFirstBytes());
        history.setRemainderHash(signature.getRemainderHash());
        history.setRemainderLength(signature.getRemainderLength());
        history.setFileType(signature.getFileType());
        history.setOffsetStart(signature.getOffsetStart());
        history.setOffsetEnd(signature.getOffsetEnd());
        history.setStatus(signature.getStatus());
        history.setUpdatedAt(signature.getUpdatedAt());
        historyRepository.save(history);

        // Определяю, какие поля изменились
        List<String> changedFields = new ArrayList<>();
        if (!signature.getThreatName().equals(signatureRequest.getThreatName())) changedFields.add("threatName");
        if (!signature.getFirstBytes().equals(signatureRequest.getFirstBytes())) changedFields.add("firstBytes");
        if (signature.getRemainderLength() != signatureRequest.getRemainderLength()) changedFields.add("remainderLength");
        if (!signature.getFileType().equals(signatureRequest.getFileType())) changedFields.add("fileType");
        if (signature.getOffsetStart() != signatureRequest.getOffsetStart()) changedFields.add("offsetStart");
        if (signature.getOffsetEnd() != signatureRequest.getOffsetEnd()) changedFields.add("offsetEnd");

        // Обновляю поля
        signature.setThreatName(signatureRequest.getThreatName());
        signature.setFirstBytes(signatureRequest.getFirstBytes());
        signature.setRemainderLength(signatureRequest.getRemainderLength());
        signature.setFileType(signatureRequest.getFileType());
        signature.setOffsetStart(signatureRequest.getOffsetStart());
        signature.setOffsetEnd(signatureRequest.getOffsetEnd());

        // Обновляю remainderHash
        String remainder = signatureRequest.getRemainder();
        if (remainder != null && !remainder.isEmpty()) {
            String remainderHash = SignatureUtils.calculateSHA256(remainder);
            if (!remainderHash.equals(signature.getRemainderHash())) changedFields.add("remainderHash");
            signature.setRemainderHash(remainderHash);
        } else {
            if (signature.getRemainderHash() != null) changedFields.add("remainderHash");
            signature.setRemainderHash(null);
        }

        SignatureEntity updatedSignature = signatureRepository.save(signature); // Сохраняю обновленную сигнатуру

        // Логирую изменения в аудит, если они есть
        if (!changedFields.isEmpty()) {
            AuditEntity audit = new AuditEntity();
            audit.setSignatureId(id);
            audit.setChangedBy(null);
            audit.setChangeType("UPDATE");
            audit.setFieldsChanged(String.join(", ", changedFields));
            auditRepository.save(audit);
        }

        return ResponseEntity.ok(updatedSignature);
    }

    // Эндпоинт для получения сигнатур по статусу
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getSignaturesByStatus(@PathVariable("status") String status) {
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("ACTUAL") && !upperStatus.equals("DELETED") && !upperStatus.equals("CORRUPTED")) {
            return ResponseEntity.badRequest().body("Неверный статус. Допустимые значения: ACTUAL, DELETED, CORRUPTED");
        }
        List<SignatureEntity> signatures = signatureRepository.findByStatus(upperStatus);
        return ResponseEntity.ok(signatures);
    }
}