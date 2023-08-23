package com.wsmrxd.bloglite.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// 在一个强类型语言和JSON打交道，那大概率是没类型安全什么事了(逃)
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void setKeyValue(String key, T value){
        var redisValOps = redisTemplate.opsForValue();
        redisValOps.set(key, value);
    }

    @Override
    public <T> T getValueByKey(String key){
        var redisValOps = redisTemplate.opsForValue();
        return (T) redisValOps.get(key);
    }

    @Override
    public <T> void putKeyValToHash(String key, String hashKey, T value){
        var redisHashOps = redisTemplate.opsForHash();
        redisHashOps.put(key, hashKey, value);
    }

    @Override
    public <T> T getValueByHashKey(String key, String hashKey){
        var redisHashOps = redisTemplate.opsForHash();
        return (T) redisHashOps.get(key, hashKey);
    }

    @Override
    public Map getHashEntriesByKey(String key){
        var redisHashOps = redisTemplate.opsForHash();
        return redisHashOps.entries(key);
    }

    @Override
    public void increaseValueByHashKey(String key, String hashKey, long delta) {
        var redisHashOps = redisTemplate.opsForHash();
        if(redisHashOps.hasKey(key, hashKey) && delta > 0)
            redisHashOps.increment(key, hashKey, delta);
    }

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
    public List getZSetAsList(String key){
        return new ArrayList<>(getZSet(key));
    }

    @Override
    public List getListByReversedScoreRange(String key, double min, double max, int offset, int num) {
        var redisZSetOps = redisTemplate.opsForZSet();
        Set<Object> set = redisZSetOps.reverseRangeByScore(key, min, max, offset, num);
        if (set != null) {
            return new ArrayList<>(set);
        }else
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
