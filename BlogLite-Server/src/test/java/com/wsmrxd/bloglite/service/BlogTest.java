package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogTest {

    @Autowired
    BlogService blogService;

    @Test
    public void testInsert(){
        var newBlog = new BlogUploadInfo();
        newBlog.setTitle("测试标题");
        newBlog.setContent("测试正文");
        var assignedID = blogService.addNewBlog(newBlog);
        System.out.println(assignedID);
        assert (assignedID > 0);
    }

    @Test
    public void testDelete(){
        var result = blogService.deleteBlog(1);
        System.out.println(result);
    }
}
