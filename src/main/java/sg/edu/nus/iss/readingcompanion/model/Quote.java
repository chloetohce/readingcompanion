package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Quote {
    private String quote;

    public Quote(String quote) {
        this.quote = quote;
    }

    public String serialize() {
        return Json.createObjectBuilder()
            .add("quote", quote.trim())
            .build()
            .toString();
    }

    public static Quote deserialize(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jobj = reader.readObject();
        return new Quote(jobj.getString("quote"));
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote.trim();
    }

    @Override
    public String toString() {
        return "Quote [quote=" + quote + "]";
    }
    
    
    
}
