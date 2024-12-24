package sg.edu.nus.iss.readingcompanion.restapi.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.readingcompanion.restapi.service.NotesAPIService;



@RestController
@RequestMapping("/api/notes")
public class NotesAPIController {
    @Autowired
    private NotesAPIService notesAPIService;

    @GetMapping("")
    public ResponseEntity<String> getNotesByBook(@RequestParam String user, @RequestParam String bookId) {
        ResponseEntity<String> response = ResponseEntity.ok()
            .body(notesAPIService.getNotesByBook(user, bookId).toString());
        return response;
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addNotesToBook(@RequestBody String entity) {
        URI uri = notesAPIService.addNotesToBook(entity);
        ResponseEntity<String> response = ResponseEntity.created(uri).build();
        return response;
    }
    
}
