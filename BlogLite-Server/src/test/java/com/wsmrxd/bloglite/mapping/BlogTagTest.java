package com.wsmrxd.bloglite.mapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogTagTest {
    @Autowired
    public void setBlogTagMapper(BlogTagMapper blogTagMapper) {
        this.blogTagMapper = blogTagMapper;
    }

    private BlogTagMapper blogTagMapper;

    @Test
    public void testSelect(){
        var resultByID = blogTagMapper.selectTagByID(3);
        var resultByName = blogTagMapper.selectTagByName("无");

        System.out.println(resultByID);
        System.out.println(resultByName);
    }

    @Test
    public void testDelete(){
        var result = blogTagMapper.deleteTagByName("new Tag");
        System.out.println(result);
    }

    @Test
    public void testUpdate(){
        var result = blogTagMapper.updateTagNameByID(8, "renamed");
        System.out.println(result);
        result = blogTagMapper.updateTagNameByName("renamed", "renamed again");
        System.out.println(result);
    }
}
