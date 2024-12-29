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
        JsonArray arrData = repo.getAllOfUser(RedisUtil.KEY_BOOKS, username);
        if (arrData == null || arrData.isEmpty()) {
            return Json.createArrayBuilder().build();
        }
        return arrData;
    }

    public URI addBookToUser(String username, String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        JsonObject book = dataJson.getJsonObject("book");

        String hashKey = username + ":" + book.getString("id");

        repo.put(RedisUtil.KEY_BOOKS, hashKey, book.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/books/details")
            .queryParam("username", username)
            .queryParam("id", book.getString("id"))
            .build(true)
            .toUri();
        
        return uri;
    }

    public String getBookDetails(String username, String id) {
        String hashkey = username + ":" + id;
        return repo.get(RedisUtil.KEY_BOOKS, hashkey);
    }

    public void deleteBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        repo.delete(RedisUtil.KEY_BOOKS, hashkey);
        repo.delete(RedisUtil.KEY_NOTES, hashkey);
        repo.delete(RedisUtil.KEY_QUOTES, hashkey);
        repo.delete(RedisUtil.KEY_WORDS, hashkey);
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
