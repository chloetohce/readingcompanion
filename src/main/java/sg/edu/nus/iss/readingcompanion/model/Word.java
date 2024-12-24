package sg.edu.nus.iss.readingcompanion.model;

import java.util.List;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

public class Word {
    private String word;
    
    private List<Map<String, String>> defintionGroups;

    public String serialize() {
        JsonObjectBuilder wordJson = Json.createObjectBuilder();
        JsonArrayBuilder grpsJson = Json.createArrayBuilder();
        for (Map<String, String> grp : defintionGroups) {
            
        }
    }
    
}
