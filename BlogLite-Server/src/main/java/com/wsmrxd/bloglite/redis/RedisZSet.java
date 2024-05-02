package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class RedisZSet {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> void addValueToZSet(String key, T value, double score){
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.add(key, value, score);
    }

    public <T> void removeZSetValue(String key, T value){
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.remove(key, value);
    }

    public long getZSetSize(String key) {
        var redisZSetOps = redisTemplate.opsForZSet();
        return Objects.requireNonNullElse(redisZSetOps.size(key), 0L);
    }

    public List getListByReversedIndexRange(String key, long start, long end){
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.reverseRange(key, start, end);
        if (set != null)
            return new ArrayList<>(set);
        else
            return null;
    }
}
