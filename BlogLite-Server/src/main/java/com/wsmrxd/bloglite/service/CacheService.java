package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.redis.*;

public interface CacheService {

    RedisKeyVal keyVal();

    RedisZSet zSet();

    void delete(String key);

    boolean hasKey(String key);
}
