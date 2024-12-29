package sg.edu.nus.iss.readingcompanion.restapi.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.readingcompanion.restapi.repository.APIRepository;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Service
public class WordsAPIService {
    @Autowired
    private APIRepository apiRepository;

    public JsonArray getWordsForBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        String result = apiRepository.get(RedisUtil.KEY_WORDS, hashkey);
        if (result == null) {
            return Json.createArrayBuilder().build();
        }
        JsonReader reader = Json.createReader(new StringReader(result));
        return reader.readArray();
    }

    public void saveWord(String username, String bookId, String request) {
        JsonReader reader = Json.createReader(new StringReader(request));
        JsonObject jObj = reader.readObject();
        JsonObject wordJson = jObj.getJsonObject("word");

        JsonArray existingWords = getWordsForBook(username, bookId);
        JsonArray newWords = Json.createArrayBuilder(existingWords)
            .add(wordJson)
            .build();
        
        // replace existing data with new data
        String hashkey = username + ":" + bookId;
        apiRepository.put(RedisUtil.KEY_WORDS, hashkey, newWords.toString());
    }

    public void deleteWord(String username, String bookId, String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        String word = dataJson.getString("word");
        String hashkey = username + ":" + bookId;

        JsonArray existingWords = getWordsForBook(username, bookId);
        JsonArrayBuilder newWords = Json.createArrayBuilder();

        for (int i = 0; i < existingWords.size(); i++) {
            JsonObject wordJson = existingWords.getJsonObject(i);
            if (!word.equals(wordJson.getString("word"))) {
                newWords.add(wordJson);
            }
        }
        apiRepository.put(RedisUtil.KEY_WORDS, hashkey, newWords.build().toString());
    }
}
