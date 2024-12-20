package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.utilities.BookJsonParser;
import sg.edu.nus.iss.readingcompanion.utilities.Helper;

public class Book {
    private String title;
    private String isbn;
    private List<String> authors;
    private List<String> genres;
    private String imageLink;

    private Optional<Date> start;
    private Optional<Date> end;
    private String status;
    
    public Book() {
    }

    public Book(String title, String isbn, List<String> authors, List<String> genres,
            String imageLink, Optional<Date> start, Optional<Date> end, String status) {
        this.title = title;
        this.isbn = isbn;
        this.authors = authors;
        this.genres = genres;
        this.imageLink = imageLink;
        this.start = start;
        this.end = end;
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
            genres,
            jsonBook.getString("imageLink"),
            BookJsonParser.getOptDateFromString(jsonBook.getString("start")),
            BookJsonParser.getOptDateFromString(jsonBook.getString("end")),
            jsonBook.getString("status")
        );
    }

    public String serialize() {
        JsonObject jsonBook = Json.createObjectBuilder()
            .add("title", title)
            .add("isbn", isbn)
            .add("authors", BookJsonParser.listToJsonArr(authors))
            .add("genres", BookJsonParser.listToJsonArr(genres))
            .add("imageLink", imageLink)
            .add("start", BookJsonParser.optDateToString(start))
            .add("end", BookJsonParser.optDateToString(end))
            .add("status", status)
            .build();
        return jsonBook.toString();
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getIsbn() {return isbn;}
    public void setIsbn(String id) {this.isbn = id;}

    public List<String> getAuthorsList() {return authors;}
    public String getAuthors() {return Helper.listToString(authors);}
    public void setAuthors(List<String> authors) {this.authors = authors;}

    public List<String> getGenresList() {return genres;}
    public String getGenres() {return Helper.listToString(genres);}
    public void setGenres(List<String> genres) {this.genres = genres;}

    public String getImageLink() {return imageLink;}
    public void setImageLink(String imageLink) {this.imageLink = imageLink;}

    public Date getStart() {return start.orElse(null);} // TODO: Fix null here and provide a different value
    public Optional<Date> getStartOpt() {return start;}
    public void setStart(Date start) {this.start = Optional.ofNullable(start);}

    public Date getEnd() {return end.get();} // TODO: Fix null here and provide a different value
    public Optional<Date> getEndOpt() {return end;}
    public void setEnd(Date end) {this.end = Optional.ofNullable(end);}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return "Book [title=" + title + ", isbn=" + isbn + ", authors=" + authors + ", genres=" + genres + ", imageLink="
                + imageLink + ", start=" + start + ", end=" + end + ", status=" + status + "]";
    }
    

}
