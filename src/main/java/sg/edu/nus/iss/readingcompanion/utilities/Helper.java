package sg.edu.nus.iss.readingcompanion.utilities;

import java.util.List;

import org.springframework.util.MultiValueMap;

public class Helper {
    public static String generateQuery(MultiValueMap<String, String> map) {
        String query = map.getOrDefault("q", List.of("")).getFirst();

        String title = map.getFirst("title");
        String author = map.getFirst("author");
        String publisher = map.getFirst("publisher");
        String isbn = map.getFirst("isbn");

        if (title != null && !title.isEmpty())
            query += " intitle:" + title;
        if (author != null && !author.isEmpty())
            query += " inauthor:" + author;
        if (publisher != null && !publisher.isEmpty())
            query += " inpublisher" + publisher;
        if (isbn != null && !isbn.isEmpty())
            query += " isbn:" + isbn;

        if (query.startsWith("+"))
            query = query.substring(1);
        return query;
    }

}
