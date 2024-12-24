package sg.edu.nus.iss.readingcompanion.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import sg.edu.nus.iss.readingcompanion.utilities.BookValParser;
import sg.edu.nus.iss.readingcompanion.utilities.URL;

@Service
public class BookService {
    private static final Logger logger = Logger.getLogger(BookService.class.getName());

    @Value("${apikey.googlebooks}")
    private String API_GOOGLEBOOKS;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Book> searchQuery(String query) {
        query = query.replace(" ", "+");
        String url = UriComponentsBuilder.fromUriString(URL.GOOGLEBOOKS)
            .queryParam("q", query)
            .queryParam("key", API_GOOGLEBOOKS)
            .toUriString();
        logger.info("Querying %s at %s".formatted(query, url));
        RequestEntity<Void> request = RequestEntity.get(url).build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JsonReader reader = Json.createReader(new StringReader(response.getBody()));
        JsonArray items = reader.readObject().getJsonArray("items");
        List<Book> searchedBooks = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            JsonObject jsonBook = items.getJsonObject(i);
            Book book = new Book();
            
            // Intermediate variables
            JsonObject volumeInfo = jsonBook.getJsonObject("volumeInfo");
            JsonArray authors = volumeInfo.getJsonArray("authors");
            JsonObject imageLinks = volumeInfo.getJsonObject("imageLinks");
            JsonArray categories = volumeInfo.getJsonArray("categories");
            JsonArray identifiers = volumeInfo.getJsonArray("industryIdentifiers");

            // Map variables
            book.setTitle(volumeInfo.getString("title"));
            book.setId(BookValParser.getIsbn(identifiers));
            book.setAuthors(BookValParser.listToString(BookValParser.jsonArrToList(authors)));
            book.setGenres(BookValParser.listToString(BookValParser.jsonArrToList(categories)));
            book.setImageLink(BookValParser.getImageLink(imageLinks, volumeInfo.getString("title")));

            book.setStart(null); // TODO: Find a cleaner way to set these variables.
            book.setEnd(null);
            book.setStatus(null);

            searchedBooks.add(book);
        }

        return searchedBooks;
    }
    
    public List<Book> getBooksByUser(String username) {
        String url = UriComponentsBuilder.fromUriString(URL.API_BOOKS)
            .pathSegment("all")       
            .queryParam("username", username)
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

    public boolean addBookToUserShelf(String username, Book book) {
        // Manipulate book details before adding
        if (book.getStatus().equals("to-read")) {
            book.setStartStr("-");
            book.setEndStr("-");
        }
        if (book.getImageLink() == null || book.getImageLink().isEmpty()) {
            String url = UriComponentsBuilder.fromUriString(URL.PLACEHOLDER_COVER)
                .queryParam("text", book.getTitle())
                .toUriString();
            book.setImageLink(url);
        }

        JsonObject data = Json.createObjectBuilder()
            .add("username", username)
            .add("book", Json.createReader(new StringReader(book.serialize())).readObject())
            .build();

        String url = UriComponentsBuilder.fromUriString(URL.API_BOOKS)
            .pathSegment("add")
            .toUriString();
        RequestEntity<String> request = RequestEntity.post(url)
            .body(data.toString());

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        logger.log(Level.INFO, "Book saved to bookshelf. Location: {0}", response.getHeaders().get("Location").getFirst());
        // TODO: Handle response if there is an error thrown
        return true;
    }

    public Book getBookDetails(String username, String bookId) {
        String uri = UriComponentsBuilder.fromUriString(URL.API_BOOKS)
            .pathSegment("details")
            .queryParam("username", username)
            .queryParam("id", bookId)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        return Book.deserialize(response.getBody());
    }

    public long getBookshelfSize(String username) {
        String uri = UriComponentsBuilder.fromUriString(URL.API_BOOKS)
            .pathSegment("size")
            .queryParam("username", username)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JsonReader reader = Json.createReader(new StringReader(response.getBody()));
        JsonObject jobj = reader.readObject();
        return jobj.getJsonNumber("size").longValueExact();
    }

    public Map<String, Long> getCounts(String username) {
        List<Book> allBooks = getBooksByUser(username);
        Map<String, Long> result = new HashMap<>();
        result.put("allSize", Long.valueOf(allBooks.size()));
        result.put("readSize", allBooks.stream().filter(b -> b.getStatus().equals("read")).count());
        result.put("readingSize", allBooks.stream().filter(b -> b.getStatus().equals("reading")).count());
        result.put("toReadSize", allBooks.stream().filter(b -> b.getStatus().equals("to-read")).count());

        List<Book> yearBooks = allBooks.stream().filter(book -> book.isThisYear()).toList();
        result.put("yearReadSize", yearBooks.stream().filter(b -> b.getStatus().equals("read")).count());

        return result;
    }

    public Map<String, Integer> getGenreCounts(String username) {
        List<Book> allBooks = getBooksByUser(username);
        Map<String, Integer> genres = new HashMap<>();
        for (Book b : allBooks) {
            List<String> bookGenres = b.getGenresList();
            for (String g : bookGenres) {
                genres.put(g, genres.getOrDefault(g, 0) + 1);
            }
        }
        return genres;
    }
}
