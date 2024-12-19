package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Book {
    private String title;
    private String id;
    private List<String> authors;
    private String publisher;
    private String isbn;
    private List<String> genres;
    private String imageLink;

    private Optional<Date> start;
    private Optional<Date> end;
    private String status;
    
    public Book() {
    }

    public Book(String title, String id, List<String> authors, String publisher, String isbn, List<String> genres,
            String imageLink, Date start, Date end, String status) {
        this.title = title;
        this.id = id;
        this.authors = authors;
        this.publisher = publisher;
        this.isbn = isbn;
        this.genres = genres;
        this.imageLink = imageLink;
        this.start = Optional.ofNullable(start);
        this.end = Optional.ofNullable(end);
        this.status = status;
    }

    /**
     * Takes in a JSON-formatted string that consists of one JSONObject, and
     * maps it to return a Book Java object.
     * @param jsonString
     * @return Book
     */
    public static Book deserialize(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonBook = reader.readObject();
        
        JsonArray jsonAuthors = jsonBook.getJsonArray("authors");
        List<String> authors = jsonAuthors.stream().map(v -> v.toString()).toList();
        
        JsonArray jsonGenres = jsonBook.getJsonArray("genres");
        List<String> genres = jsonGenres.stream().map(v -> v.toString()).toList();

        return new Book(
            jsonBook.getString("title"),
            jsonBook.getString("id"),
            authors,
            jsonBook.getString("publisher"),
            jsonBook.getString("isbn"),
            genres,
            jsonBook.getString("imageLink"),
            new Date(jsonBook.getJsonNumber("start").longValueExact()),
            new Date(jsonBook.getJsonNumber("end").longValueExact()),
            jsonBook.getString("status")
        );
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public List<String> getAuthors() {return authors;}
    public void setAuthors(List<String> authors) {this.authors = authors;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public String getIsbn() {return isbn;}
    public void setIsbn(String isbn) {this.isbn = isbn;}

    public List<String> getGenres() {return genres;}
    public void setGenres(List<String> genres) {this.genres = genres;}

    public String getImageLink() {return imageLink;}
    public void setImageLink(String imageLink) {this.imageLink = imageLink;}

    public Optional<Date> getStart() {return start;}
    public void setStart(Date start) {this.start = Optional.ofNullable(start);}

    public Optional<Date> getEnd() {return end;}
    public void setEnd(Date end) {this.end = Optional.ofNullable(end);}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

}
