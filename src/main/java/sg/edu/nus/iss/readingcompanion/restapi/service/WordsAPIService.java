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

    public String getWordsForBook(String username, String bookId) {
        String hashkey = username + ":" + bookId;
        String result = apiRepository.get(RedisUtil.KEY_WORDS, hashkey);
        System.out.println(result);
        if (result == null) {
            return Json.createArrayBuilder().build().toString();
        }
        return result;
    }

    public void saveWord(String request) {
        JsonReader reader = Json.createReader(new StringReader(request));
        JsonObject jObj = reader.readObject();
        String username = jObj.getString("username");
        String bookId = jObj.getString("bookId");
        JsonObject wordJson = jObj.getJsonObject("word");

        JsonReader readerRepoData = Json.createReader(new StringReader(getWordsForBook(username, bookId)));
        JsonArray existingWords = readerRepoData.readArray();
        JsonArray newWords = Json.createArrayBuilder(existingWords)
            .add(wordJson)
            .build();
        
        // replace existing data with new data
        String hashkey = username + ":" + bookId;
        apiRepository.put(RedisUtil.KEY_WORDS, hashkey, newWords.toString());
    }

    public void deleteWord(String data) {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject dataJson = reader.readObject();
        String username = dataJson.getString("username");
        String bookId = dataJson.getString("bookId");
        String word = dataJson.getString("word");
        String hashkey = username + ":" + bookId;

        JsonReader readerRepoData = Json.createReader(new StringReader(getWordsForBook(username, bookId)));
        JsonArray existingWords = readerRepoData.readArray();
        System.out.println(existingWords);
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
