package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisHash {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public <T> void putKeyValToHash(String key, String hashKey, T value){
        var redisHashOps = redisTemplate.opsForHash();
        redisHashOps.put(key, hashKey, value);
    }

    public <T> T getValueByHashKey(String key, String hashKey){
        var redisHashOps = redisTemplate.opsForHash();
        return (T) redisHashOps.get(key, hashKey);
    }

    public Map getHashEntriesByKey(String key){
        var redisHashOps = redisTemplate.opsForHash();
        return redisHashOps.entries(key);
    }

    public void increaseValueByHashKey(String key, String hashKey, long delta) {
        var redisHashOps = redisTemplate.opsForHash();
        if(delta > 0)
            redisHashOps.increment(key, hashKey, delta);
    }
}
