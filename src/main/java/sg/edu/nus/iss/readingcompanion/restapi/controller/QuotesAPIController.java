package sg.edu.nus.iss.readingcompanion.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.readingcompanion.restapi.service.QuotesAPIService;

@RestController
@RequestMapping("/api/quotes")
public class QuotesAPIController {
    @Autowired
    private QuotesAPIService quotesAPIService;
    
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addWord(@RequestParam String username, @RequestParam String bookId, @RequestBody String request) {
        quotesAPIService.saveQuote(username, bookId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getWordsForBook(@RequestParam String username, @RequestParam String bookId) {
        String quotes = quotesAPIService.getQuotesForBook(username, bookId);
        if (quotes != null && !quotes.equals("")) {
            return ResponseEntity.ok().body(quotes);
        } else {
            JsonObject jobj = Json.createObjectBuilder()
                .add("message", "No quotes found for book.")
                .build();
            ResponseEntity<String> response = ResponseEntity.unprocessableEntity()
                .body(jobj.toString());
            return response;
        }
    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBook(@RequestParam String username, @RequestParam String bookId, @RequestBody String data) {
        quotesAPIService.deleteQuote(username, bookId, data);
        return ResponseEntity.ok().build();
    }
}
