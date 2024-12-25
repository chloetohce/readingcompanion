package sg.edu.nus.iss.readingcompanion.restapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.readingcompanion.restapi.service.WordsAPIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/words")
public class WordsAPIController {
    @Autowired
    private WordsAPIService wordsAPIService;
    
    @PostMapping("/add")
    public ResponseEntity<String> addWord(@RequestBody String request) {
        wordsAPIService.saveWord(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<String> getWordsForBook(@RequestParam String username, @RequestParam String bookId) {
        String words = wordsAPIService.getWordsForBook(username, bookId);
        if (words != null) {
            return ResponseEntity.ok().body(words);
        } else {
            JsonObject jobj = Json.createObjectBuilder()
                .add("message", "No words found for book.")
                .build();
            ResponseEntity<String> response = ResponseEntity.unprocessableEntity()
                .body(jobj.toString());
            return response;
        }
    }
    
    
}
