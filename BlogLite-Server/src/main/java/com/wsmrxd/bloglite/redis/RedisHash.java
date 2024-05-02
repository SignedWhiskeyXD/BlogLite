package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisHash {

    private final HashOperations<String, Object, Object> redisHashOps;

    @Autowired
    public RedisHash(RedisTemplate<String, Object> redisTemplate) {
        this.redisHashOps = redisTemplate.opsForHash();
    }

    public <T> void putKeyValToHash(String key, String hashKey, T value){
        redisHashOps.put(key, hashKey, value);
    }

    public <T> T getValueByHashKey(String key, String hashKey){
        return (T) redisHashOps.get(key, hashKey);
    }

    public Map getHashEntriesByKey(String key){
        return redisHashOps.entries(key);
    }

    public void increaseValueByHashKey(String key, String hashKey, long delta) {
        if(delta <= 0) return;

        redisHashOps.increment(key, hashKey, delta);
    }
}
