package sg.edu.nus.iss.readingcompanion.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @PostMapping("/add")
    public ResponseEntity<String> addWord(@RequestBody String request) {
        quotesAPIService.saveQuote(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<String> getWordsForBook(@RequestParam String username, @RequestParam String bookId) {
        String quotes = quotesAPIService.getQuotesForBook(username, bookId);
        System.out.println(quotes);
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
}
