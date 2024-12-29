package sg.edu.nus.iss.readingcompanion.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.readingcompanion.restapi.service.QuotesAPIService;

@RestController
@RequestMapping("/api/quotes")
public class QuotesAPIController {
    @Autowired
    private QuotesAPIService quotesAPIService;
    
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addWord(@RequestParam String username, @RequestParam String bookId, @RequestBody String request) {
        try {
            quotesAPIService.saveQuote(username, bookId, request);
            return ResponseEntity.ok().build();
        } catch (JsonParseException | NullPointerException | ClassCastException e) {
            return ResponseEntity.badRequest().body("Error reading input data.");
        }
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getWordsForBook(@RequestParam String username, @RequestParam String bookId) {
        String quotes = quotesAPIService.getQuotesForBook(username, bookId);
        if (quotes != null && !quotes.equals("")) {
            return ResponseEntity.ok().body(quotes);
        } else {
            ResponseEntity<String> response = ResponseEntity.notFound().build();
            return response;
        }
    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBook(@RequestParam String username, @RequestParam String bookId, @RequestBody String data) {
        try {
            quotesAPIService.deleteQuote(username, bookId, data);
            return ResponseEntity.ok().build();
        } catch (JsonParseException | NullPointerException | ClassCastException e) {
            return ResponseEntity.badRequest().body("Error reading input data.");
        }
    }
}
