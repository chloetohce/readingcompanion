package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.BookService;


@Controller
@RequestMapping("/charts")
public class ChartsController {
    @Autowired
    private BookService bookService;

    @GetMapping("")
    public String displayChart(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        model.addAllAttributes(bookService.getCounts(username));
        model.addAttribute("genresCount", bookService.getGenreCounts(username));
        return "charts";
    }
    
}
