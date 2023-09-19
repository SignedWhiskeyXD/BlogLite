package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisHyperLogLog {

    private HyperLogLogOperations<String, Object> hyperLogLogOps;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.hyperLogLogOps = redisTemplate.opsForHyperLogLog();
    }

    public <T> void add(String key, T value){
        hyperLogLogOps.add(key, value);
    }

    public long count(String key){
        return hyperLogLogOps.size(key);
    }
}
