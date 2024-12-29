package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.utilities.BookValParser;

public class Word {
    private String word;
    
    private Map<String, List<String>> definitions;

    public Word(String word, Map<String, List<String>> definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public String serialize() {
        JsonObjectBuilder wordJson = Json.createObjectBuilder();
        JsonArrayBuilder definitionsJson = Json.createArrayBuilder();
        for (Entry<String, List<String>> entry : definitions.entrySet()) {
            JsonObject part = Json.createObjectBuilder()
                    .add("partOfSpeech", entry.getKey())
                    .add("definitions", BookValParser.listToJsonArr(entry.getValue()))
                    .build();
            definitionsJson.add(part);
        }
        JsonObject jObj = wordJson.add("word", word)
            .add("meanings", definitionsJson.build())
            .build();
        return jObj.toString();
    }
 
    public static Word deserialize(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jObj = reader.readObject();
        String word = jObj.getString("word");
        JsonArray allMeanings = jObj.getJsonArray("meanings");

        Map<String, List<String>> meanings = new HashMap<>();

        for (int i = 0; i < allMeanings.size(); i++) {
            JsonObject part = allMeanings.getJsonObject(i);
            String partOfSpeech = part.getString("partOfSpeech");
            List<String> definitionsList = BookValParser.jsonArrToList(part.getJsonArray("definitions"));
            meanings.put(partOfSpeech, definitionsList);
        }

        return new Word(word, meanings);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<String, List<String>> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, List<String>> definitions) {
        this.definitions = definitions;
    }

    @Override
    public String toString() {
        return "Word [word=" + word + ", definitions=" + definitions + "]";
    }

    
    
}
