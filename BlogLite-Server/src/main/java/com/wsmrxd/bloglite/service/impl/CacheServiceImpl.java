package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.redis.*;
import com.wsmrxd.bloglite.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisKeyVal redisKeyVal;

    @Autowired
    private RedisList redisList;

    @Autowired
    private RedisSet redisSet;

    @Autowired
    private RedisHash redisHash;

    @Autowired
    private RedisHyperLogLog redisHyperLogLog;

    @Autowired
    private RedisZSet redisZSet;

    @Override
    public void delete(String key){
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public RedisKeyVal keyVal() {
        return this.redisKeyVal;
    }

    @Override
    public RedisHash hash() {
        return this.redisHash;
    }

    @Override
    public RedisSet set() {
        return this.redisSet;
    }

    @Override
    public RedisList list() {
        return this.redisList;
    }

    @Override
    public RedisZSet zSet() {
        return this.redisZSet;
    }

    @Override
    public RedisHyperLogLog hyperLogLog() {
        return this.redisHyperLogLog;
    }
}
