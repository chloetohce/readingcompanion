package sg.edu.nus.iss.readingcompanion.utilities;

import java.util.List;

import jakarta.json.JsonArray;

public class Helper {
    public static List<String> jsonArrToList(JsonArray arr) {
        return arr.stream().map(v -> v.toString()).toList();
    }
}
