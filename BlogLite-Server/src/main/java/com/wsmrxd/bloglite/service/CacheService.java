package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.redis.*;

public interface CacheService {

    RedisKeyVal keyVal();

    RedisHash hash();

    RedisSet set();

    RedisList list();

    RedisZSet zSet();

    RedisHyperLogLog hyperLogLog();

    void delete(String key);

    boolean hasKey(String key);
}
