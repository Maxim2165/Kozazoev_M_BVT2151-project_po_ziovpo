package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
// Отмечает класс как REST-контроллер и задаёт базовый путь /api для всех эндпоинтов
public class MyRestController {

    private final MyService myService;

    // Конструктор с внедрением зависимости MyService через Spring
    public MyRestController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/hello")
    public String getMessage() {
        // Обработчик GET-запроса /api/hello, возвращает сообщение из MyService
        return myService.getMessage();
    }

    @GetMapping("/echo")
    public String echo(@RequestParam String str) {
        // Обработчик GET-запроса /api/echo, возвращает переданный параметр str
        return str;
    }

}
