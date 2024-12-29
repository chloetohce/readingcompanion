package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.readingcompanion.model.Quote;
import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.WordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;

    @PostMapping("/add")
    public String addWord(@AuthenticationPrincipal User user, @RequestBody MultiValueMap<String, String> form) {
        String bookId = form.getFirst("bookId");
        wordService.saveWord(user.getUsername(), bookId, form.getFirst("newWord"));

        return "redirect:/books/details/" + bookId + "?view=words";
    }

    @PostMapping("/delete")
    public String deleteQuote(@AuthenticationPrincipal User user, @RequestBody MultiValueMap<String, String> form) {
        String bookId = form.getFirst("bookId");
        String wordText = form.getFirst("wordText");
        wordService.deleteWord(user.getUsername(), bookId, wordText);
        return "redirect:/books/details/" + bookId + "?view=words";
    }
    

}
