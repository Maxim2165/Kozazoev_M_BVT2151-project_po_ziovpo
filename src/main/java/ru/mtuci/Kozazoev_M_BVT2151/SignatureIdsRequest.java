package ru.mtuci.Kozazoev_M_BVT2151;

import java.util.List;
import java.util.UUID;

// DTO для приема списка UUID от клиента
public class SignatureIdsRequest {
    private List<UUID> ids;

    // Геттер
    public List<UUID> getIds() {
        return ids;
    }

    // Сеттер
    public void setIds(List<UUID> ids) {
        this.ids = ids;
    }
}
