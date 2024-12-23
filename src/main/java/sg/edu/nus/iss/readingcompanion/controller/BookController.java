package sg.edu.nus.iss.readingcompanion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import sg.edu.nus.iss.readingcompanion.model.Book;
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
    
    @PostMapping("/add")
    public String addToBookshelf(@ModelAttribute Book book, @AuthenticationPrincipal User user) {
        bookService.addBookToUserShelf(user.getUsername(), book);
        return "redirect:/books/all";
    }
    

    @GetMapping("/search")
    public String search() {
        return "search";
    }

    @PostMapping("/search")
    public String searchResult(@RequestParam String q, Model model) {
        model.addAttribute("searchResult", bookService.searchQuery(q));
        return "search-result";
    }
    
    @GetMapping("details/{id}")
    public String getBookDetails(@PathVariable String id, @AuthenticationPrincipal User user, Model model) {
        Book book = bookService.getBookDetails(user.getUsername(), id);
        model.addAttribute("book", book); // TODO: Default value for start and end dates
        return "book-details";

    }

    @GetMapping("/edit/{id}")
    public String bookDetailsForm(@PathVariable String id, @AuthenticationPrincipal User user, Model model) {
        Book book = bookService.getBookDetails(user.getUsername(), id);
        model.addAttribute("book", book);
        return "book-form";
    }
    
    @PostMapping("/edit") // TODO: Change URL here to something more generic, e.g. /put
    public String editBookDetails(@Valid @ModelAttribute Book book, BindingResult binding, @AuthenticationPrincipal User user) {
        if (binding.hasErrors()) {
            return "book-form";
        }

        bookService.addBookToUserShelf(user.getUsername(), book);
        return "redirect:/books/details/" + book.getId();
    }

    @GetMapping("/add")
    public String bookForm(Model model) {
        model.addAttribute("book", Book.manualInput());
        return "book-form";
    }
    
    
}
