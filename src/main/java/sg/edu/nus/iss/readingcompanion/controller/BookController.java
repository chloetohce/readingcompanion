package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/books")
public class BookController {
    
    @GetMapping("/all")
    public String bookshelf(Model model) {
        return "bookshelf";
    }
    
    @GetMapping("/add")
    public String addBook(@RequestParam String param) {
        return new String();
    }
    
}
