package sg.edu.nus.iss.readingcompanion.service;

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
        restTemplate.exchange(request, String.class);

        return true;

    }
}
