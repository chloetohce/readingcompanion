package sg.edu.nus.iss.readingcompanion.utilities;

import java.util.List;

import org.springframework.util.MultiValueMap;

public class Helper {
    public static String generateQuery(MultiValueMap<String, String> map) {
        String query = map.getOrDefault("any", List.of("")).getFirst();

        if (!map.getFirst("title").isEmpty())
            query += " intitle:" + map.getFirst("title");
        if (!map.getFirst("author").isEmpty())
            query += " inauthor:" + map.getFirst("author");
        if (!map.getFirst("publisher").isEmpty())
            query += " inpublisher" + map.getFirst("publisher");
        if (!map.getFirst("isbn").isEmpty())
            query += " isbn:" + map.getFirst("isbn");

        if (query.startsWith("+"))
            query = query.substring(1);
        return query;
    }

}
