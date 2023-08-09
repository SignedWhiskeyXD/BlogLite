package com.wsmrxd.bloglite.mapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogTest {

    @Autowired
    private BlogMapper blogMapper;

    @Test
    public void testSelectLatestID(){
        System.out.println(blogMapper.selectLatestBlogIDs(1919810, 2));
    }

}
