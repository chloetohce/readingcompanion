package sg.edu.nus.iss.readingcompanion.restapi.repository;

import java.util.Map.Entry;

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
import sg.edu.nus.iss.readingcompanion.utilities.RedisUtil;

@Repository
public class BookAPIRepository {
    @Autowired
    @Qualifier(RedisUtil.TEMPLATE)
    private RedisTemplate<String, String> template;

    private HashOperations<String, String, String> ops;

    @PostConstruct
    @SuppressWarnings("unused")
    private void init() {
        this.ops = template.opsForHash();
    }

    public JsonArray getByUser(String redisKey, String username) {
        ScanOptions options = ScanOptions.scanOptions()
            .match(username + "_*")
            .build();
        JsonArrayBuilder books = Json.createArrayBuilder();
        Cursor<Entry<String, String>> cur = ops.scan(redisKey, options);
        while (cur.hasNext()) {
            System.out.println(cur.next());
           books.add(cur.next().getValue());
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

}
