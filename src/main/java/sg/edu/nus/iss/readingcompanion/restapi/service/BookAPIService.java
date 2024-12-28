package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.io.StringReader;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.restapi.repository.APIRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class BookAPIService {
    @Autowired
    private APIRepository repo;

    public JsonArray getAllBooksByUser(String username) {
        return repo.getAllOfUser(RedisUtil.KEY_BOOKS, username);
    }

    public URI addBookToUser(String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        JsonObject book = dataJson.getJsonObject("book");

        String hashKey = dataJson.getString("username") + ":" + book.getString("id");
        // TODO: Error handling for if the book ID already exists in the bookshelf. Only important if the book is manually add. Idea is to have a custom prefix for manual books

        repo.put(RedisUtil.KEY_BOOKS, hashKey, book.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/books/details")
            .queryParam("username", dataJson.getString("username"))
            .queryParam("id", book.getString("id"))
            .build(true)
            .toUri();
        
        return uri;
    }

    public String getBookDetails(String username, String id) {
        String hashkey = username + ":" + id;
        return repo.get(RedisUtil.KEY_BOOKS, hashkey); // TODO: Add error handling for if book does not exist.
    }

    public void deleteBook(String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        String username = dataJson.getString("username");
        String id = dataJson.getString("id");
        String hashkey = username + ":" + id;
        repo.delete(RedisUtil.KEY_BOOKS, hashkey);
    }

    public String getSizeOfBookshelf(String username) {
        long size = repo.size(RedisUtil.KEY_BOOKS, username);
        JsonObject obj = Json.createObjectBuilder()
            .add("username", username)
            .add("size", size)
            .build();
        return obj.toString();
    }
}
