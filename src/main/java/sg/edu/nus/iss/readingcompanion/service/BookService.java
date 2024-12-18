package sg.edu.nus.iss.readingcompanion.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.model.Book;
import sg.edu.nus.iss.readingcompanion.utilities.URL;

@Service
public class BookService {
    @Value("${apikey.googlebooks}")
    private String API_GOOGLEBOOKS;

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

    public List<Book> searchQuery(String query) {
        String url = UriComponentsBuilder.fromUriString(URL.GOOGLEBOOKS)
            .queryParam("q", query)
            .queryParam("key", API_GOOGLEBOOKS)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(url).build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JsonReader reader = Json.createReader(new StringReader(response.getBody()));
        JsonArray items = reader.readObject().getJsonArray("items");

        for (int i = 0; i < items.size(); i++) {
            JsonObject jsonBook = items.getJsonObject(i);
            Book book = new Book();
            
            // Intermediate variables
            JsonObject volumeInfo = jsonBook.getJsonObject("volumeInfo");
            JsonArray authors = volumeInfo.getJsonArray("authors");
            JsonArray identifiers = volumeInfo.getJsonArray("industryIdentifiers");
            JsonObject imageLinks = volumeInfo.getJsonObject("imageLinks");
            JsonArray categories = volumeInfo.getJsonArray("categories");

            
        }
    }
}
