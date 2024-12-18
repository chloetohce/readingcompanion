package sg.edu.nus.iss.readingcompanion.restapi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import sg.edu.nus.iss.readingcompanion.constant.RedisUtil;

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

    public List<String> getByUser(String redisKey, String username) {
        ScanOptions options = ScanOptions.scanOptions()
            .match(username + "_*")
            .build();
        List<String> values = new ArrayList<>();
        Cursor<Entry<String, String>> cur = ops.scan(redisKey, options);
        while (cur.hasNext()) {
           values.add(cur.next().getValue());
        }
        return values;
    }

    public boolean put(String redisKey, String hashKey, String value) {
        ops.put(redisKey, hashKey, value);
        return true;
    }

}
