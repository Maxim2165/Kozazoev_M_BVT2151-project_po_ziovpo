package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Контроллер для тестовых эндпоинтов
@RestController
@RequestMapping("/api")
public class MyRestController {
    private final MyService myService;

    public MyRestController(MyService myService) {
        this.myService = myService; // Внедряю сервис
    }

    // Эндпоинт для получения тестового сообщения
    @GetMapping("/hello")
    public String getMessage() {
        return myService.getMessage(); // Возвращаю сообщение из сервиса
    }

    // Эндпоинт для эхо-ответа
    @GetMapping("/echo")
    public String echo(@RequestParam String str) {
        return str; // Возвращаю переданную строку
    }
}