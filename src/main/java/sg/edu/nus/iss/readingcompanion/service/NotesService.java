package sg.edu.nus.iss.readingcompanion.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.readingcompanion.model.Notes;
import sg.edu.nus.iss.readingcompanion.utilities.URL;

@Service
public class NotesService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = Logger.getLogger(NotesService.class.getName());

    public Notes getNotes(String username, String bookId) {
        String url = UriComponentsBuilder.fromUriString(URL.API_NOTES)
            .queryParam("username", username)
            .queryParam("bookId", bookId)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(url)
            .build();

        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            return Notes.deserialize(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.info("USER: %s with BOOK: %s has no associated notes. Creating new note.".formatted(username, bookId));
            return new Notes(bookId, "");
        }
    }

    public boolean saveNotes(String username, String bookId, Notes notes) {
        JsonObject notesJson = Json.createObjectBuilder()
            .add("bookId", notes.getBookId())
            .add("text", notes.getText())
            .build();
        String uri = UriComponentsBuilder.fromUriString(URL.API_NOTES)
                .pathSegment("add")
                .queryParam("username", username)
                .queryParam("bookId", bookId)
                .toUriString();
        RequestEntity<String> request = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(notesJson.toString());
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        logger.log(Level.INFO, "Book saved to bookshelf. Location: {0}",
                response.getHeaders().get("Location").getFirst());
        return true;

    }
}
