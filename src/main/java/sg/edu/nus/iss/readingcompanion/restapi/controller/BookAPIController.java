package sg.edu.nus.iss.readingcompanion.restapi.controller;

import java.net.URI;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserBookshelf(@RequestParam String username) {
        JsonArray bookshelf = bookAPIService.getAllBooksByUser(username);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(bookshelf.toString());
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveBook(@RequestParam String username, @RequestBody String data) {
        URI uri = bookAPIService.addBookToUser(username, data);
        
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteBook(@RequestParam String username, @RequestParam String bookId) {
        bookAPIService.deleteBook(username, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE) //TODO: Error handling if book does not exist
    public ResponseEntity<String> getBookDetails(@RequestParam String username, @RequestParam String bookId) {
        ResponseEntity<String> response = ResponseEntity.ok()
            .body(bookAPIService.getBookDetails(username, bookId));
        return response;
    }

    @GetMapping(path = "/size", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserBookshelfSize(@RequestParam String username) {
        String msg = bookAPIService.getSizeOfBookshelf(username);
        ResponseEntity<String> response = ResponseEntity.ok(msg);
        return response;
    }
    
    
    
}
