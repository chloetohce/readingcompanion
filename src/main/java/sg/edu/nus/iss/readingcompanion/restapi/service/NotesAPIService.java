package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.io.StringReader;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.restapi.repository.APIRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class NotesAPIService {
    @Autowired
    private APIRepository repo;

    public JsonObject getNotesByBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        String notesData = repo.get(RedisUtil.KEY_NOTES, hashkey);
        JsonReader reader = Json.createReader(new StringReader(notesData));
        JsonObject jObj = reader.readObject();
        return jObj;
    }

    public URI addNotesToBook(String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        String hashKey = dataJson.getString("user") + ":" + dataJson.getString("bookId");
        JsonObject note = dataJson.getJsonObject("notes");

        repo.put(RedisUtil.KEY_NOTES, hashKey, note.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/notes")
            .queryParam("user", dataJson.getString("user"))
            .queryParam("bookId", dataJson.getString("bookId"))
            .build(true)
            .toUri();
        
        return uri;
    }
}
