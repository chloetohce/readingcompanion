package sg.edu.nus.iss.readingcompanion.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class BookJsonParser {
    private static final Logger logger = Logger.getLogger(BookJsonParser.class.getName());

    public static List<String> jsonArrToList(JsonArray arr) {
        if (arr == null) {
            logger.log(Level.WARNING, "Array is null.");
            List<String> result = new ArrayList<>();
            result.add("NA");
            return result;
        }

        return arr.stream().map(v -> v.toString()).toList();
    }

    public static String getIsbn(JsonArray arr) {
        // If there is no ISBN provided, generate own
        if (arr == null) {
            return UUID.randomUUID().toString();
        }

        // Prioritise ISBN 13
        for (int i = 0; i < arr.size(); i++) {
            JsonObject isbnJson = arr.getJsonObject(i);
            if (isbnJson.getString("type").equals("ISBN_13")) {
                return isbnJson.getString("identifier");
            }
        }

        // If ISBN does not exist, get ISBN 10
        for (int i = 0; i < arr.size(); i++) {
            JsonObject isbnJson = arr.getJsonObject(i);
            if (isbnJson.getString("type").startsWith("ISBN")) {
                return isbnJson.getString("identifier");
            }
        }

        // If no ISBN available, get any available
        return arr.getJsonObject(0).getString("identifier");
    }

    public static String getImageLink(JsonObject imageJson, String title) {
        if (imageJson == null || imageJson.containsKey("thumbnail") == false) {
            String url = UriComponentsBuilder.fromUriString(URL.PLACEHOLDER_COVER)
                .queryParam("text", title)
                .toUriString();
            return url;
        }

        return imageJson.getString("thumbnail").replace("&edge=curl", "");
    }

}
