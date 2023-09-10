package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

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
    public <T> boolean setKeyValueIfAbsent(String key, T value, Duration timeout){
        var redisValOps = redisTemplate.opsForValue();
        return Boolean.TRUE.equals(redisValOps.setIfAbsent(key, value, timeout));
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
    public <T> void rPushValToList(String key, T value) {
        var redisListOps = redisTemplate.opsForList();
        redisListOps.rightPush(key, value);
    }

    @Override
    public List getList(String key) {
        var redisListOps = redisTemplate.opsForList();
        return redisListOps.range(key, 0, -1);
    }

    @Override
    public List getListByRange(String key, long startIndex, long endIndex) {
        var redisListOps = redisTemplate.opsForList();
        return redisListOps.range(key, startIndex, endIndex);
    }

    @Override
    public long getListSize(String key) {
        var redisListOps = redisTemplate.opsForList();
        return Objects.requireNonNullElse(redisListOps.size(key), 0L);
    }

    @Override
    public Map getHashEntriesByKey(String key){
        var redisHashOps = redisTemplate.opsForHash();
        return redisHashOps.entries(key);
    }

    @Override
    public void increaseValueByHashKey(String key, String hashKey, long delta) {
        var redisHashOps = redisTemplate.opsForHash();
        if(delta > 0)
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
