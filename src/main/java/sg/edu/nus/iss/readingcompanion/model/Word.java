package sg.edu.nus.iss.readingcompanion.model;

import java.io.StringReader;
import java.util.ArrayList;
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
    
    private List<Map<String, List<String>>> defintionGroups;

    public Word(String word, List<Map<String, List<String>>> defintionGroups) {
        this.word = word;
        this.defintionGroups = defintionGroups;
    }

    public String serialize() {
        JsonObjectBuilder wordJson = Json.createObjectBuilder();
        JsonArrayBuilder grpsJson = Json.createArrayBuilder();
        for (Map<String, List<String>> grp : defintionGroups) {
            // Map = definition group for word
            JsonArrayBuilder defGroup = Json.createArrayBuilder();

            for (Entry<String, List<String>> entry : grp.entrySet()) {
                // String = partOfSpeech and List<String> = list of defintions
                JsonObject part = Json.createObjectBuilder()
                    .add("partOfSpeech", entry.getKey())
                    .add("defintiions", BookValParser.listToJsonArr(entry.getValue()))
                    .build();
                defGroup.add(part);
            }
            grpsJson.add(defGroup.build());
        }

        JsonObject jObj = wordJson.add("word", word)
            .add("groups", grpsJson.build())
            .build();
        return jObj.toString();
    }
 
    public static Word deserialize(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jObj = reader.readObject();
        String word = jObj.getString("word");
        JsonArray groups = jObj.getJsonArray("groups");
        List<Map<String, List<String>>> groupsList = new ArrayList<>();

        for (int i = 0; i < groups.size(); i++) {
            JsonArray group = groups.getJsonArray(i);
            Map<String, List<String>> groupMap = new HashMap<>();
            
            for (int j = 0; j < group.size(); j++) {
                JsonObject part = group.getJsonObject(j);
                String partOfSpeech = part.getString("partOfSpeech");
                JsonArray definitions = part.getJsonArray("definitions");
                List<String> definitionsList = BookValParser.jsonArrToList(definitions);
                groupMap.put(partOfSpeech, definitionsList);
            }
            groupsList.add(groupMap);
        }
        return new Word(word, groupsList);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Map<String, List<String>>> getDefintionGroups() {
        return defintionGroups;
    }

    public void setDefintionGroups(List<Map<String, List<String>>> defintionGroups) {
        this.defintionGroups = defintionGroups;
    }

    @Override
    public String toString() {
        return "Word [word=" + word + ", defintionGroups=" + defintionGroups + "]";
    }

    
}
