package com.wsmrxd.bloglite.util;

import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testStringVal(){
        var redisStringOps = redisTemplate.opsForValue();

        System.out.println(redisStringOps.get("yaju"));
    }

    @Test
    public void testJsonVal(){
        var obj = new User("wsmrxd@gmail.com", "Whiskey", "1919810", "admin");
        var valueOps = redisTemplate.opsForValue();
        valueOps.set("wsmrxd", obj);

        System.out.println(valueOps.get("wsmrxd"));
    }

    @Test
    public void testHash(){
        var hashOps = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();

        var user = new User("wsmrxd@gmail.com", "Whiskey", "1919810", "admin");
        map.put("User", user);

        var tag = new BlogTag();
        tag.setTagName("tag");
        map.put("BlogTag", tag);

        hashOps.putAll("hashTest", map);
        User resultUser = (User) hashOps.get("hashTest", "User");
        BlogTag resultTag = (BlogTag) hashOps.get("hashTest", "BlogTag");

        assert resultTag != null && resultUser != null;
    }
}
