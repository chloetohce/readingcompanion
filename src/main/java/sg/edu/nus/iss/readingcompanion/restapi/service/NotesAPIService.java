package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import sg.edu.nus.iss.readingcompanion.model.Notes;
import sg.edu.nus.iss.readingcompanion.restapi.repository.APIRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class NotesAPIService {
    @Autowired
    private APIRepository repo;

    public String getNotesByBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        String notesData = repo.get(RedisUtil.KEY_NOTES, hashkey);
        return notesData;
    }

    public URI addNotesToBook(String username, String bookId, String data) {
        //Check for errors in data
        Notes.deserialize(data);
        String hashKey = username + ":" + bookId;

        repo.put(RedisUtil.KEY_NOTES, hashKey, data);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/notes")
            .queryParam("username", username)
            .queryParam("bookId", bookId)
            .build(true)
            .toUri();
        
        return uri;
    }

}
