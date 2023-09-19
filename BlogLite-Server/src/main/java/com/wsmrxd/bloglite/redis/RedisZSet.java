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

    public <T> T getZSetValueByScore(String key, double score){
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.rangeByScore(key, score, score, 0, 1);
        if(set != null && !set.isEmpty())
            return (T) set.toArray()[0];
        else
            return null;
    }

    public <T> void addValueToZSet(String key, T value, double score){
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.add(key, value, score);
    }

    public void removeZSetValueByScore(String key, double score) {
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.removeRangeByScore(key, score, score);
    }

    public Set getZSet(String key){
        var redisZSetOps = redisTemplate.opsForZSet();
        return redisZSetOps.range(key, 0, -1);
    }

    public long getZSetSize(String key) {
        var redisZSetOps = redisTemplate.opsForZSet();
        return Objects.requireNonNullElse(redisZSetOps.size(key), 0L);
    }

    public List getZSetAsList(String key){
        return new ArrayList<>(getZSet(key));
    }

    public List getListByReversedScoreRange(String key, double min, double max, int offset, int num) {
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.reverseRangeByScore(key, min, max, offset, num);
        if (set != null)
            return new ArrayList<>(set);
        else
            return null;
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
