package ru.mtuci.Kozazoev_M_BVT2151;

import java.util.Objects;
import java.util.UUID;

// DTO для результата сканирования
public class SignatureScanResult {
    private String signatureId;
    private String threatName;
    private int offsetFromStart;
    private int offsetFromEnd;
    private boolean matched;

    public SignatureScanResult(UUID signatureId, String threatName, int offsetFromStart, int offsetFromEnd, boolean matched) {
        this.signatureId = signatureId != null ? signatureId.toString() : null; // Конвертирую UUID в строку
        this.threatName = threatName;
        this.offsetFromStart = offsetFromStart;
        this.offsetFromEnd = offsetFromEnd;
        this.matched = matched;
    }

    public String getSignatureId() {
        return signatureId; // Возвращаю ID сигнатуры
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId; // Устанавливаю ID сигнатуры
    }

    public String getThreatName() {
        return threatName; // Возвращаю название угрозы
    }

    public void setThreatName(String threatName) {
        this.threatName = threatName; // Устанавливаю название угрозы
    }

    public int getOffsetFromStart() {
        return offsetFromStart; // Возвращаю смещение начала
    }

    public void setOffsetFromStart(int offsetFromStart) {
        this.offsetFromStart = offsetFromStart; // Устанавливаю смещение начала
    }

    public int getOffsetFromEnd() {
        return offsetFromEnd; // Возвращаю смещение конца
    }

    public void setOffsetFromEnd(int offsetFromEnd) {
        this.offsetFromEnd = offsetFromEnd; // Устанавливаю смещение конца
    }

    public boolean isMatched() {
        return matched; // Возвращаю, найдена ли сигнатура
    }

    public void setMatched(boolean matched) {
        this.matched = matched; // Устанавливаю, найдена ли сигнатура
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignatureScanResult that = (SignatureScanResult) o;
        return offsetFromStart == that.offsetFromStart &&
                offsetFromEnd == that.offsetFromEnd &&
                matched == that.matched &&
                Objects.equals(signatureId, that.signatureId) &&
                Objects.equals(threatName, that.threatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signatureId, threatName, offsetFromStart, offsetFromEnd, matched);
    }
}