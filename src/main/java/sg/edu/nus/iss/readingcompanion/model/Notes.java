package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Notes {
    private String bookId;

    private String text;
    
    public Notes() {
    }

    public Notes(String bookId, String text) {
        this.bookId = bookId;
        this.text = text;
    }

    public static Notes deserialize(String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject noteJson = reader.readObject();
        return new Notes(noteJson.getString("bookId"), noteJson.getString("text"));
    }

    public String serialize() {
        JsonObject noteJson = Json.createObjectBuilder()
            .add("bookId", bookId)
            .add("text", text)
            .build();
        return noteJson.toString();
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
}
