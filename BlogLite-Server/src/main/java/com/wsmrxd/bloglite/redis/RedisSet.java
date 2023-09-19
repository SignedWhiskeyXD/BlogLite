package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RedisSet {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> void addValueToSet(String key, T value) {
        var redisSetOps = redisTemplate.opsForSet();
        redisSetOps.add(key, value);
    }

    public <T> void removeSetValue(String key, T value) {
        var redisSetOps = redisTemplate.opsForSet();
        redisSetOps.remove(key, value);
    }

    public Set getSet(String key) {
        var redisSetOps = redisTemplate.opsForSet();
        return redisSetOps.members(key);
    }

    public List getSetAsList(String key) {
        return new ArrayList<>(getSet(key));
    }

    public <T> void addValueToZSet(String key, T value, double score){
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.add(key, value, score);
    }
}
