package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.BookService;



@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    
    @GetMapping("/all")
    public String bookshelf(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("books", bookService.getBooksByUser(user.getUsername()));
        return "bookshelf";
    }
    
    @GetMapping("/add")
    public String addBook(@RequestParam String param) {
        return new String();
    }
    
}
