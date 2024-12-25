package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.restapi.repository.APIRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class QuotesAPIService {
    @Autowired
    private APIRepository apiRepository;

    public String getQuotesForBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        String result = apiRepository.get(RedisUtil.KEY_QUOTES, hashkey);
        if (result == null) {
            return Json.createArrayBuilder().build().toString();
        }
        return result;
    }

    public void saveQuote(String request) {
        JsonReader reader = Json.createReader(new StringReader(request));
        JsonObject jObj = reader.readObject();
        String username = jObj.getString("username");
        String bookId = jObj.getString("bookId");
        JsonObject quoteJson = jObj.getJsonObject("quote");

        JsonReader readerRepoData = Json.createReader(new StringReader(getQuotesForBook(username, bookId)));
        JsonArray existingWords = readerRepoData.readArray();
        JsonArray newQuotes = Json.createArrayBuilder(existingWords)
            .add(quoteJson)
            .build();
        
        // replace existing data with new data
        String hashkey = username + ":" + bookId;
        apiRepository.put(RedisUtil.KEY_QUOTES, hashkey, newQuotes.toString());
    }

}
