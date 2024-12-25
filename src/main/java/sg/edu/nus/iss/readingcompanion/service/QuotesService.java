package sg.edu.nus.iss.readingcompanion.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.model.Quote;
import sg.edu.nus.iss.readingcompanion.utilities.URL;

@Service
public class QuotesService {
    private RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = Logger.getLogger(QuotesService.class.getName());

    public boolean saveQuote(String username, String bookId, String formInput) {
        JsonObject requestJson = Json.createObjectBuilder()
            .add("username", username)
            .add("bookId", bookId)
            .add("quote", Json.createReader(new StringReader(new Quote(formInput).serialize())).readObject())
            .build();
        String uri = UriComponentsBuilder.fromUriString(URL.API_QUOTES)
                .pathSegment("add")
                .toUriString();
        RequestEntity<String> request = RequestEntity.post(uri)
            .body(requestJson.toString());
        
        restTemplate.exchange(request, String.class);
        return true;
    }

    public List<Quote> getQuotesForBook(String username, String bookId) {
        String uri = UriComponentsBuilder.fromUriString(URL.API_QUOTES)
            .queryParam("username", username)
            .queryParam("bookId", bookId)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            JsonReader reader = Json.createReader(new StringReader(response.getBody()));
            JsonArray quotesArr = reader.readArray();
            logger.info(quotesArr.toString());

            List<Quote> quotes = new ArrayList<>();
            for (int i = 0; i < quotesArr.size(); i++) {
                JsonObject quotesJson = quotesArr.getJsonObject(i);
                Quote q = Quote.deserialize(quotesJson.toString());
                quotes.add(q);
            }
            return quotes;
        
        } catch (HttpClientErrorException e) {
            logger.info("USER: %s with BOOK: %s has no associated quotes.".formatted(username, bookId));
            return new ArrayList<>();
        }
    }
}
