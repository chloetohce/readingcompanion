package sg.edu.nus.iss.readingcompanion.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import sg.edu.nus.iss.readingcompanion.model.Word;
import sg.edu.nus.iss.readingcompanion.utilities.URL;

@Service
public class WordService {
    private static final Logger logger = Logger.getLogger(WordService.class.getName());
    private RestTemplate restTemplate = new RestTemplate();

    public Word queryWord(String word) throws Exception {
        String uri = UriComponentsBuilder.fromUriString(URL.WORDSAPI)
            .pathSegment(word)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(uri).build();

        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            
            JsonReader reader = Json.createReader(new StringReader(response.getBody()));
            JsonArray groups = reader.readArray();
            List<Map<String, List<String>>> groupsList = new ArrayList<>();

            for (int i = 0; i < groups.size(); i++) {
                JsonObject group = groups.getJsonObject(i);
                JsonArray parts = group.getJsonArray("meanings");
                Map<String, List<String>> partMap = new HashMap<>();

                for (int j = 0; j < parts.size(); j++) {
                    JsonObject part = parts.getJsonObject(j);
                    String partOfSpeech = part.getString("partOfSpeech");
                    JsonArray definitions = part.getJsonArray("definitions");
                    List<String> definitionsList = new ArrayList<>();

                    for (int k = 0; k < definitions.size(); k++) {
                        String definition = definitions.getJsonObject(k).getString("definition");
                        definitionsList.add(definition);
                    }
                    partMap.put(partOfSpeech, definitionsList);
                }
                groupsList.add(partMap);
            }

            return new Word(word, groupsList);
            
        } catch (HttpClientErrorException e) {
            throw new Exception("Word not found.");
        }
    }

    public boolean saveWord(String username, String bookId, String word) {
        try {
            Word toSave = queryWord(word);
            String uri = UriComponentsBuilder.fromUriString(URL.API_WORD)
                .pathSegment("add")
                .toUriString();
            JsonObject requestBody = Json.createObjectBuilder()
                .add("username", username)
                .add("bookId", bookId)
                .add("word", Json.createReader(new StringReader(toSave.serialize())).readObject())
                .build();
            RequestEntity<String> request = RequestEntity.post(uri).body(requestBody.toString());
            restTemplate.exchange(request, String.class);
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    public List<Word> getWordsForBook(String username, String bookId) {
        String uri = UriComponentsBuilder.fromUriString(URL.API_WORD)
            .queryParam("username", username)
            .queryParam("bookId", bookId)
            .toUriString();
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            JsonReader reader = Json.createReader(new StringReader(response.getBody()));
            JsonArray wordsArr = reader.readArray();
    
            List<Word> words = new ArrayList<>();
            for (int i = 0; i < wordsArr.size(); i++) {
                JsonObject wordJson = wordsArr.getJsonObject(i);
                Word w = Word.deserialize(wordJson.toString());
                words.add(w);
            }
    
            return words;
        } catch (HttpClientErrorException e) {
            logger.info("USER: %s with BOOK: %s has no associated words.".formatted(username, bookId));
            return new ArrayList<>();
        }
    }

}
