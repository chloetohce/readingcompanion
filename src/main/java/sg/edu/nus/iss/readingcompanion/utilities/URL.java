package sg.edu.nus.iss.readingcompanion.utilities;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class URL {
    public static final String API_BOOKS = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/api/books"; //TODO: Do I need to change this if I deploy to railway

    public static final String API_NOTES = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() +  "/api/notes";

    public static final String API_WORD = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() +  "/api/words";
    
    public static final String API_QUOTES = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() +  "/api/quotes";

    public static final String GOOGLEBOOKS = "https://www.googleapis.com/books/v1/volumes";
    
    public static final String PLACEHOLDER_COVER = "https://images.placeholders.dev/?width=128&height=195&textWrap=true&fontSize=10";

    public static final String WORDSAPI = "https://api.dictionaryapi.dev/api/v2/entries/en/";
}
