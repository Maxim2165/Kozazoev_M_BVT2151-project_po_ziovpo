package ru.mtuci.Kozazoev_M_BVT2151;

// DTO для приема данных от клиента
class SignatureRequest {
    private String threatName;
    private String firstBytes;
    private String remainder; // "Хвост" сигнатуры
    private Integer remainderLength;
    private String fileType;
    private Integer offsetStart;
    private Integer offsetEnd;

    // Геттеры и сеттеры
    public String getThreatName() {
        return threatName;
    }

    public void setThreatName(String threatName) {
        this.threatName = threatName;
    }

    public String getFirstBytes() {
        return firstBytes;
    }

    public void setFirstBytes(String firstBytes) {
        this.firstBytes = firstBytes;
    }

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }

    public Integer getRemainderLength() {
        return remainderLength;
    }

    public void setRemainderLength(Integer remainderLength) {
        this.remainderLength = remainderLength;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getOffsetStart() {
        return offsetStart;
    }

    public void setOffsetStart(Integer offsetStart) {
        this.offsetStart = offsetStart;
    }

    public Integer getOffsetEnd() {
        return offsetEnd;
    }

    public void setOffsetEnd(Integer offsetEnd) {
        this.offsetEnd = offsetEnd;
    }
}
