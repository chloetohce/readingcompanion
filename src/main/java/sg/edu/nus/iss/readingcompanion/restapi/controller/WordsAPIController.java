package sg.edu.nus.iss.readingcompanion.restapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.readingcompanion.restapi.service.WordsAPIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addWord(@RequestParam String username, @RequestParam String bookId, @RequestBody String request) {
        wordsAPIService.saveWord(username, bookId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getWordsForBook(@RequestParam String username, @RequestParam String bookId) {
        JsonArray words = wordsAPIService.getWordsForBook(username, bookId);
        if (words != null && !words.isEmpty()) {
            return ResponseEntity.ok().body(words.toString());
        } else {
            JsonObject jobj = Json.createObjectBuilder()
                .add("message", "No words found for book.")
                .build();
            ResponseEntity<String> response = ResponseEntity.unprocessableEntity()
                .body(jobj.toString());
            return response;
        }
    }
    
    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteWord(@RequestParam String username, @RequestParam String bookId, @RequestBody String data) {
        wordsAPIService.deleteWord(username, bookId, data);
        return ResponseEntity.ok().build();
    }
    
}
