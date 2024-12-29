package sg.edu.nus.iss.readingcompanion.restapi.controller;

import java.net.URI;

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
import sg.edu.nus.iss.readingcompanion.restapi.service.NotesAPIService;



@RestController
@RequestMapping("/api/notes")
public class NotesAPIController {
    @Autowired
    private NotesAPIService notesAPIService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getNotesByBook(@RequestParam String username, @RequestParam String bookId) {
        String notes = notesAPIService.getNotesByBook(username, bookId);
        if (notes == null) {
            JsonObject jobj = Json.createObjectBuilder()
                .add("message", "Book id " + bookId + " was not found.")
                .build();
            ResponseEntity<String> response = ResponseEntity.unprocessableEntity()
                .body(jobj.toString());
            return response;
        }
        ResponseEntity<String> response = ResponseEntity.ok()
            .body(notes);
        return response;
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addNotesToBook(@RequestParam String username, @RequestParam String bookId, @RequestBody String entity) {
        URI uri = notesAPIService.addNotesToBook(username, bookId, entity);
        ResponseEntity<String> response = ResponseEntity.created(uri).build();
        return response;
    }
    
}
