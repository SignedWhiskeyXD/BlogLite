package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisKeyVal {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public <T> void setKeyValue(String key, T value){
        var redisValOps = redisTemplate.opsForValue();
        redisValOps.set(key, value);
    }

    public <T> boolean setKeyValueIfAbsent(String key, T value, Duration timeout){
        var redisValOps = redisTemplate.opsForValue();
        return Boolean.TRUE.equals(redisValOps.setIfAbsent(key, value, timeout));
    }

    public <T> T getValueByKey(String key){
        var redisValOps = redisTemplate.opsForValue();
        return (T) redisValOps.get(key);
    }
}
