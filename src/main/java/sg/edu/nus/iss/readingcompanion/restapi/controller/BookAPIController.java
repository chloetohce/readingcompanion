package sg.edu.nus.iss.readingcompanion.restapi.controller;

import java.io.StringReader;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.restapi.service.BookAPIService;



@RestController
@RequestMapping("/api/books")
public class BookAPIController {
    private static final Logger logger = Logger.getLogger(BookAPIController.class.getName());

    @Autowired
    private BookAPIService bookAPIService;
    
    @GetMapping("/all")
    public ResponseEntity<String> getUserBookshelf(@RequestParam("user") String username) {
        JsonArray bookshelf = bookAPIService.getAllBooksByUser(username);
        System.out.println(bookshelf.toString());

        return ResponseEntity.ok(bookshelf.toString());
    }

    @PostMapping("/add") // TODO: Change book here
    public ResponseEntity<String> saveBook(@RequestBody String data) {
        bookAPIService.addBookToUser(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/books")
            .build(true)
            .toUri();
        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/details")
    public ResponseEntity<String> getBookDetails(@RequestParam String user, @RequestParam String id) {
        ResponseEntity<String> response = ResponseEntity.ok()
            .body(bookAPIService.getBookDetails(user, id));
        return response;
    }
    
    
    
}
