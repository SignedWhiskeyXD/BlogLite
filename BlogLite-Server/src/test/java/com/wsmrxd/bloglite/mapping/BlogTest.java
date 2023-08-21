package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogTest {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private RedisService redisService;

    @Test
    public void testAllBlogIDs(){
        var result1 = redisService.getBlogIDsStartAt(114514, 5);
        var result2 = redisService.getBlogIDsStartAt(3, 5);
        System.out.println(result1);
        System.out.println(result2);
    }
}
