package sg.edu.nus.iss.readingcompanion.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonArray;
import sg.edu.nus.iss.readingcompanion.model.Book;
import sg.edu.nus.iss.readingcompanion.restapi.service.BookAPIService;



@RestController
@RequestMapping("/api/books")
public class BookAPIController {
    @Autowired
    private BookAPIService bookAPIService;
    
    @GetMapping("/all")
    public ResponseEntity<String> getUserBookshelf(@RequestParam("user") String username) {
        JsonArray bookshelf = bookAPIService.getAllBooksByUser(username);

        return ResponseEntity.ok(bookshelf.toString());
    }

    @PostMapping("/add")
    public ResponseEntity<String> saveBook(@RequestBody Book book, @RequestParam("user") String user) {
        //TODO: add Book to user's bookshelf
        
        return null;
    }
    
    
}
