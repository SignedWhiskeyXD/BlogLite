package com.wsmrxd.bloglite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RedisList {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> void rPushValToList(String key, T value) {
        var redisListOps = redisTemplate.opsForList();
        redisListOps.rightPush(key, value);
    }

    public List getList(String key) {
        var redisListOps = redisTemplate.opsForList();
        return redisListOps.range(key, 0, -1);
    }

    public List getListByRange(String key, long startIndex, long endIndex) {
        var redisListOps = redisTemplate.opsForList();
        return redisListOps.range(key, startIndex, endIndex);
    }

    public long getListSize(String key) {
        var redisListOps = redisTemplate.opsForList();
        return Objects.requireNonNullElse(redisListOps.size(key), 0L);
    }
}
