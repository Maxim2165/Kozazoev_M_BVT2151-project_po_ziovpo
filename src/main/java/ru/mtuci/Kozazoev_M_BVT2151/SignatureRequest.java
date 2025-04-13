package ru.mtuci.Kozazoev_M_BVT2151;

// DTO для получения данных сигнатуры от клиента
public class SignatureRequest {
    private String threatName;
    private String firstBytes;
    private String remainder; // Остаток сигнатуры
    private Integer remainderLength;
    private String fileType;
    private Integer offsetStart;
    private Integer offsetEnd;

    public String getThreatName() {
        return threatName; // Возвращаю название угрозы
    }

    public void setThreatName(String threatName) {
        this.threatName = threatName; // Устанавливаю название угрозы
    }

    public String getFirstBytes() {
        return firstBytes; // Возвращаю первые байты
    }

    public void setFirstBytes(String firstBytes) {
        this.firstBytes = firstBytes; // Устанавливаю первые байты
    }

    public String getRemainder() {
        return remainder; // Возвращаю остаток
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder; // Устанавливаю остаток
    }

    public Integer getRemainderLength() {
        return remainderLength; // Возвращаю длину остатка
    }

    public void setRemainderLength(Integer remainderLength) {
        this.remainderLength = remainderLength; // Устанавливаю длину остатка
    }

    public String getFileType() {
        return fileType; // Возвращаю тип файла
    }

    public void setFileType(String fileType) {
        this.fileType = fileType; // Устанавливаю тип файла
    }

    public Integer getOffsetStart() {
        return offsetStart; // Возвращаю смещение начала
    }

    public void setOffsetStart(Integer offsetStart) {
        this.offsetStart = offsetStart; // Устанавливаю смещение начала
    }

    public Integer getOffsetEnd() {
        return offsetEnd; // Возвращаю смещение конца
    }

    public void setOffsetEnd(Integer offsetEnd) {
        this.offsetEnd = offsetEnd; // Устанавливаю смещение конца
    }
}