package sg.edu.nus.iss.readingcompanion.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class BookValParser {
    private static final Logger logger = Logger.getLogger(BookValParser.class.getName());

    public static List<String> jsonArrToList(JsonArray arr) {
        if (arr == null) { 
            logger.log(Level.WARNING, "Array is null.");
            List<String> result = new ArrayList<>();
            result.add("NA");
            return result;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(arr.getJsonString(i).getString());
        }
        return list;
    }

    public static JsonArray listToJsonArr(List<String> list) {
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (String s : list) {
            arr.add(s);
        }
        return arr.build();
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

    public static String optDateToString(Optional<Date> opt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return opt.map(d -> sdf.format(d)).orElse("-");
    }

    public static Optional<Date> getOptDateFromString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return Optional.ofNullable(sdf.parse(date));
        } catch (ParseException | NullPointerException e) {
            return Optional.empty();
        }
    }

    public static List<String> stringToList(String s) {
        String[] arr = s.split(",");
        List<String> list = new ArrayList<>();

        for (String str : arr) {
            str = str.trim();
            if (!str.equals(""))
                list.add(str.trim());
        }
        return list;
    }

    public static String listToString(List<String> list) {
        String result = "";
        if (list == null || list.isEmpty()) {
            return result;
        }
        for (String e : list) {
            result += e.trim();
            result += ", ";
        }
        return result.substring(0, result.length() - 2);
    }

}
