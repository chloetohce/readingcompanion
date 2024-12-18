package sg.edu.nus.iss.readingcompanion.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.constant.URL;
import sg.edu.nus.iss.readingcompanion.model.Book;

@Service
public class BookService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Book> getBooksByUser(String username) {
        String url = UriComponentsBuilder.fromUriString(URL.API)
            .queryParam("user", username)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(url)
            .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        JsonReader reader = Json.createReader(new StringReader(response.getBody()));
        JsonArray bookshelfArr = reader.readArray();

        List<Book> bookshelf = new ArrayList<>();
        
        for (int i = 0; i < bookshelfArr.size(); i++) {
            JsonObject jsonBook = bookshelfArr.getJsonObject(i);
            bookshelf.add(Book.deserialize(jsonBook.toString()));
        }
        return bookshelf;
    }
}
