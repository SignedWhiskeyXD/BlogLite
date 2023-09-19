package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

// 在一个强类型语言和JSON打交道，那大概率是没类型安全什么事了(逃)
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> T getZSetValueByScore(String key, double score){
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.rangeByScore(key, score, score, 0, 1);
        if(set != null && !set.isEmpty())
            return (T) set.toArray()[0];
        else
            return null;
    }

    @Override
    public <T> void addValueToSet(String key, T value) {
        var redisSetOps = redisTemplate.opsForSet();
        redisSetOps.add(key, value);
    }

    @Override
    public <T> void removeSetValue(String key, T value) {
        var redisSetOps = redisTemplate.opsForSet();
        redisSetOps.remove(key, value);
    }

    @Override
    public Set getSet(String key) {
        var redisSetOps = redisTemplate.opsForSet();
        return redisSetOps.members(key);
    }

    @Override
    public List getSetAsList(String key) {
        return new ArrayList<>(getSet(key));
    }

    @Override
    public <T> void addValueToZSet(String key, T value, double score){
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.add(key, value, score);
    }

    @Override
    public void removeZSetValueByScore(String key, double score) {
        var redisZSetOps = redisTemplate.opsForZSet();
        redisZSetOps.removeRangeByScore(key, score, score);
    }

    @Override
    public Set getZSet(String key){
        var redisZSetOps = redisTemplate.opsForZSet();
        return redisZSetOps.range(key, 0, -1);
    }

    @Override
    public long getZSetSize(String key) {
        var redisZSetOps = redisTemplate.opsForZSet();
        return Objects.requireNonNullElse(redisZSetOps.size(key), 0L);
    }

    @Override
    public List getZSetAsList(String key){
        return new ArrayList<>(getZSet(key));
    }

    @Override
    public List getListByReversedScoreRange(String key, double min, double max, int offset, int num) {
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.reverseRangeByScore(key, min, max, offset, num);
        if (set != null)
            return new ArrayList<>(set);
        else
            return null;
    }

    @Override
    public List getListByReversedIndexRange(String key, long start, long end){
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.reverseRange(key, start, end);
        if (set != null)
            return new ArrayList<>(set);
        else
            return null;
    }

    @Override
    public void delete(String key){
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
