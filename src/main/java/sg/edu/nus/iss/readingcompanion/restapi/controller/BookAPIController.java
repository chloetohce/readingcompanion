package sg.edu.nus.iss.readingcompanion.restapi.controller;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.model.Book;
import sg.edu.nus.iss.readingcompanion.restapi.service.BookAPIService;



@RestController
@RequestMapping("/api/books")
public class BookAPIController {
    @Autowired
    private BookAPIService bookAPIService;
    
    @GetMapping("/all")
    public ResponseEntity<String> getUserBookshelf(@RequestParam("user") String username) {
        List<String> rawData = bookAPIService.getAllBooksByUser(username);
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (String book : rawData) {
            JsonReader reader = Json.createReader(new StringReader(book));
            JsonObject bookJson = reader.readObject();
            arrBuilder.add(bookJson);
        }

        return ResponseEntity.ok(arrBuilder.build().toString());
    }

    @PostMapping("/add")
    public ResponseEntity<String> saveBook(@RequestBody Book book, @RequestParam("user") String user) {
        //TODO: add Book to user's bookshelf
        
        return null;
    }
    
    
}
