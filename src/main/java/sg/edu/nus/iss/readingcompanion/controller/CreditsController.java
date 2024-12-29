package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/credits")
public class CreditsController {
    @GetMapping("")
    public String creditsPage() {
        return "credits";
    }
    
}
