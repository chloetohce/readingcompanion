package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.WordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/quote")
public class QuoteController {
    @Autowired
    private WordService wordService;

    @PostMapping("/add")
    public String addQuote(@AuthenticationPrincipal User user, @RequestBody MultiValueMap<String, String> form) {
        String bookId = form.getFirst("bookId");
        wordService.saveWord(user.getUsername(), bookId, form.getFirst("newQuote"));

        return "redirect:/books/details/" + bookId;
    }

}
