package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import sg.edu.nus.iss.readingcompanion.utilities.BookValParser;
import sg.edu.nus.iss.readingcompanion.validation.ValidDate;

public class Book {
    @NotBlank(message = "Book title is required.")
    private String title;

    @NotBlank
    private String id;

    @NotEmpty(message = "Please provide authors of book. Leave NA if there are no authors.")
    private String authorsStr; // Temp store of values to enable validation
    private List<String> authors;

    @NotEmpty(message = "Genres cannot be left empty. Leave NA if there are no genres.")
    private String genresStr;
    private List<String> genres;

    @NotBlank
    private String imageLink;

    @ValidDate(message = "Start date cannot be in the future. ")
    private String startStr;
    private Optional<Date> start;

    @ValidDate(message = "End date cannot be in the future. ")
    private String endStr;
    private Optional<Date> end;

    @NotBlank(message = "What's your current status on the book?")
    private String status;
    
    public Book() {
    }

    public Book(String title, String id, List<String> authors, List<String> genres,
            String imageLink, Optional<Date> start, Optional<Date> end, String status) {
        this.title = title;
        this.id = id;
        this.authors = authors;
        this.authorsStr = BookValParser.listToString(authors);
        this.genres = genres;
        this.genresStr = BookValParser.listToString(genres);
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
        List<String> authors = BookValParser.jsonArrToList(jsonAuthors);
        
        JsonArray jsonGenres = jsonBook.getJsonArray("genres");
        List<String> genres = BookValParser.jsonArrToList(jsonGenres);

        return new Book(
            jsonBook.getString("title"),
            jsonBook.getString("id"),
            authors,
            genres,
            jsonBook.getString("imageLink"),
            BookValParser.getOptDateFromString(jsonBook.getString("start")),
            BookValParser.getOptDateFromString(jsonBook.getString("end")),
            jsonBook.getString("status")
        );
    }

    public String serialize() {
        JsonObject jsonBook = Json.createObjectBuilder()
            .add("title", title)
            .add("id", id)
            .add("authors", BookValParser.listToJsonArr(authors))
            .add("genres", BookValParser.listToJsonArr(genres))
            .add("imageLink", imageLink)
            .add("start", BookValParser.optDateToString(start))
            .add("end", BookValParser.optDateToString(end))
            .add("status", status)
            .build();
        return jsonBook.toString();
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public List<String> getAuthorsList() {return authors;}
    public String getAuthors() {return BookValParser.listToString(authors);}
    public void setAuthors(String authors) {this.authors = BookValParser.stringToList(authors);}

    public String getAuthorsStr() {
        return BookValParser.listToString(authors);
    }
    public void setAuthorsStr(String authorsStr) {
        this.authorsStr = authorsStr;
        this.authors = BookValParser.stringToList(authorsStr);
    }

    public List<String> getGenresList() {return genres;}
    public String getGenres() {return BookValParser.listToString(genres);}
    public void setGenres(String genres) {this.genres = BookValParser.stringToList(genres);}

    public String getGenresStr() {
        return BookValParser.listToString(genres);
    }
    public void setGenresStr(String genresStr) {
        this.genresStr = genresStr;
        this.genres = BookValParser.stringToList(genresStr);
    }

    public String getImageLink() {return imageLink;}
    public void setImageLink(String imageLink) {this.imageLink = imageLink;}

    public String getStart() {return BookValParser.optDateToString(start);}
    public Optional<Date> getStartOpt() {return start;}
    public void setStart(String start) {this.start = BookValParser.getOptDateFromString(start);}

    public String getStartStr() {
        return BookValParser.optDateToString(start);
    }
    public void setStartStr(String startStr) {
        this.startStr = startStr;
        this.start = BookValParser.getOptDateFromString(startStr);
    }

    public String getEnd() {return BookValParser.optDateToString(end);}
    public Optional<Date> getEndOpt() {return end;}
    public void setEnd(String end) {this.end = BookValParser.getOptDateFromString(end);}

    public String getEndStr() {
        return BookValParser.optDateToString(end);
    }
    public void setEndStr(String endStr) {
        this.endStr = endStr;
        this.end = BookValParser.getOptDateFromString(endStr);
    }

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return "Book [title=" + title + ", id=" + id + ", authors=" + authors + ", genres=" + genres + ", imageLink="
                + imageLink + ", start=" + start + ", end=" + end + ", status=" + status + "]";
    }
    

}
