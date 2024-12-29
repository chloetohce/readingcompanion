package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/api/docs")
public class APIController {
    
    @GetMapping("")
    public String documentationPage() {
        return "docs";
    }
    
}
