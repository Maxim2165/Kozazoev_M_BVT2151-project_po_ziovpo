package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// Сервис для тестового сообщения
@Component
@Service
public class MyService {
    public String getMessage() {
        return "Это мой первый проект на JAVA по сканированию файлов"; // Возвращаю тестовое сообщение
    }
}