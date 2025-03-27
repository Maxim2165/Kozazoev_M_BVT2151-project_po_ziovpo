package ru.mtuci.Kozazoev_M_BVT2151;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Component
@Service
@Repository
@Controller
@RestController
public class MyService {
    public String getMessage() {
        return "Это мой первый проект на JAVA по сканированию файлов";
    }
}


