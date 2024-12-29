package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.readingcompanion.restapi.repository.APIRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class QuotesAPIService {
    @Autowired
    private APIRepository apiRepository;

    public String getQuotesForBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        String result = apiRepository.get(RedisUtil.KEY_QUOTES, hashkey);
        return result;
    }

    public void saveQuote(String username, String bookId, String quote) {
        JsonReader readerQuote = Json.createReader(new StringReader(quote));
        String quotesStr = getQuotesForBook(username, bookId);
        if (quotesStr == null) {
            quotesStr = Json.createArrayBuilder().build().toString();
        }
        JsonReader readerRepoData = Json.createReader(new StringReader(quotesStr));
        JsonArray existingQuotes = readerRepoData.readArray();
        JsonArray newQuotes = Json.createArrayBuilder(existingQuotes)
            .add(readerQuote.readObject().getJsonObject("quote"))
            .build();
        
        // replace existing data with new data
        String hashkey = username + ":" + bookId;
        apiRepository.put(RedisUtil.KEY_QUOTES, hashkey, newQuotes.toString());
    }

    public void deleteQuote(String username, String bookId, String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        String hashkey = username + ":" + bookId;
        
        JsonReader readerRepoData = Json.createReader(new StringReader(getQuotesForBook(username, bookId)));
        JsonArray existingQuotes = readerRepoData.readArray();
        JsonArrayBuilder newQuotes = Json.createArrayBuilder();
        
        for (JsonValue q : existingQuotes) {
            if (!q.equals(dataJson)) {
                newQuotes.add(q);
            }
        }

        apiRepository.put(RedisUtil.KEY_QUOTES, hashkey, newQuotes.build().toString());
    }

}
