package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.constant.RedisUtil;
import sg.edu.nus.iss.readingcompanion.restapi.repository.BookAPIRepository;

@Service
public class BookAPIService {
    @Autowired
    private BookAPIRepository bookAPIRepository;

    public List<String> getAllBooksByUser(String username) {
        return bookAPIRepository.getByUser(RedisUtil.KEY_BOOKS, username);
    }

    public void addBookToUser(String username, String book) {
        JsonReader reader = Json.createReader(new StringReader(book));
        JsonObject bookJson = reader.readObject();
        String hashKey = username + ":" + bookJson.getString("id");
        // TODO: Error handling for if the book ID already exists in the bookshelf. Only important if the book is manually add. Idea is to have a custom prefix for manual books

        bookAPIRepository.put(RedisUtil.KEY_BOOKS, hashKey, book);
    }
}
