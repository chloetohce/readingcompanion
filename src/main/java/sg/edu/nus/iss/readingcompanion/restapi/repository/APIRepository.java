package sg.edu.nus.iss.readingcompanion.restapi.repository;

import java.io.StringReader;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Repository
public class APIRepository {
    @Autowired
    @Qualifier(RedisUtil.TEMPLATE)
    private RedisTemplate<String, String> template;

    private HashOperations<String, String, String> ops;
    private static final Logger logger = Logger.getLogger(APIRepository.class.getName());

    @PostConstruct
    @SuppressWarnings("unused")
    private void init() {
        this.ops = template.opsForHash();
    }

    public JsonArray getAllOfUser(String redisKey, String username) {
        ScanOptions options = ScanOptions.scanOptions()
            .match(username + ":*")
            .build();
        JsonArrayBuilder books = Json.createArrayBuilder();
        Cursor<Entry<String, String>> cur = ops.scan(redisKey, options);
        while (cur.hasNext()) {
            JsonObject book = Json.createReader(new StringReader(cur.next().getValue())).readObject();
            books.add(book);
        }
        return books.build();
    }

    public boolean put(String redisKey, String hashKey, String value) {
        ops.put(redisKey, hashKey, value);
        return true;
    }

    public String get(String redisKey, String hashKey) {
        return ops.get(redisKey, hashKey);
    }

    public void delete(String redisKey, String hashKey) {
        ops.delete(redisKey, hashKey);
    }

    public long size(String redisKey, String username) {
        ScanOptions options = ScanOptions.scanOptions()
            .match(username + ":*")
            .build();
        long count = 0;
        Cursor<Entry<String, String>> cur = ops.scan(redisKey, options);
        while (cur.hasNext()) {
            count += 1;
            cur.next();
        }
        return count;
    }

}
