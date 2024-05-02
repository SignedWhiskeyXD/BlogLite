package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.Duration;

@Component
public class RedisKeyVal {

    private final ValueOperations<String, Object> redisValOps;

    @Autowired
    public RedisKeyVal(RedisTemplate<String, Object> redisTemplate) {
        this.redisValOps = redisTemplate.opsForValue();
    }

    public <T> void setKeyValue(String key, T value){
        redisValOps.set(key, value);
    }

    public <T> boolean setKeyValueIfAbsent(String key, T value, Duration timeout){
        return Boolean.TRUE.equals(redisValOps.setIfAbsent(key, value, timeout));
    }

    @Nullable
    public <T> T getValueByKey(String key){
        return (T) redisValOps.get(key);
    }
}
