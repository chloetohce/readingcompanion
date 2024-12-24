package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.readingcompanion.model.Notes;
import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.NotesService;


@Controller
@RequestMapping("/notes")
public class NotesController {
    @Autowired
    private NotesService notesService;

    @PostMapping("/edit")
    public String editNotes(@AuthenticationPrincipal User user, @ModelAttribute Notes notes) {
        String bookId = notes.getBookId();
        notesService.saveNotes(user.getUsername(), bookId, notes);
        return "redirect:/books/details/" + bookId;
    }
    
}
