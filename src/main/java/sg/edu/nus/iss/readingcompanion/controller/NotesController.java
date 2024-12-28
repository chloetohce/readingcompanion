package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.readingcompanion.model.Notes;
import sg.edu.nus.iss.readingcompanion.model.User;
import sg.edu.nus.iss.readingcompanion.service.BookService;
import sg.edu.nus.iss.readingcompanion.service.NotesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/notes")
public class NotesController {
    @Autowired
    private NotesService notesService;

    @Autowired
    private BookService bookService;

    @GetMapping("/edit")
    public String editNote(@AuthenticationPrincipal User user, @RequestParam String bookId, Model model) {
        model.addAttribute("notes", notesService.getNotes(user.getUsername(), bookId));
        model.addAttribute("book", bookService.getBookDetails(user.getUsername(), bookId));
        return "notes-form";
    }
    

    @PostMapping("/edit")
    public String editNotes(@AuthenticationPrincipal User user, @ModelAttribute Notes notes) {
        String bookId = notes.getBookId();
        notesService.saveNotes(user.getUsername(), bookId, notes);
        return "redirect:/books/details/" + bookId;
    }
    
}
