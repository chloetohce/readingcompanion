package sg.edu.nus.iss.readingcompanion.restapi.controller;

import java.net.URI;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonArray;
import sg.edu.nus.iss.readingcompanion.restapi.service.BookAPIService;

@RestController
@RequestMapping("/api/books")
public class BookAPIController {
    private static final Logger logger = Logger.getLogger(BookAPIController.class.getName());

    @Autowired
    private BookAPIService bookAPIService;
    
    @GetMapping("/all")
    public ResponseEntity<String> getUserBookshelf(@RequestParam String username) {
        JsonArray bookshelf = bookAPIService.getAllBooksByUser(username);
        return ResponseEntity.ok(bookshelf.toString());
    }

    @PostMapping("/add")
    public ResponseEntity<String> saveBook(@RequestBody String data) {
        URI uri = bookAPIService.addBookToUser(data);
        
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/details") //TODO: Error handling if book does not exist
    public ResponseEntity<String> getBookDetails(@RequestParam String username, @RequestParam String id) {
        ResponseEntity<String> response = ResponseEntity.ok()
            .body(bookAPIService.getBookDetails(username, id));
        return response;
    }
    
    
    
}
