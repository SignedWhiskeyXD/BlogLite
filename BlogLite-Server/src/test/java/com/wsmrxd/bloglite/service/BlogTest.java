package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
    public void testSelect(){
        var blog = blogService.getBlogByID(1);
        System.out.println(blog);

        blogService.renameBlogTitle(1, "修改标题");
        blogService.editBlogContent(1, "修改正文");
        var newBlog = blogService.getBlogByID(1);
        System.out.println(newBlog);
    }

    @Test
    public void testDelete(){
        var result = blogService.deleteBlog(1);
        System.out.println(result);
    }

    @Test
    public void testBlogTags(){
        var result = blogService.getAllTagsByBlogID(22);
        System.out.println(result);
    }

    @Test
    public void testBlogTagRemapping(){
        List<String> newTagNames = new ArrayList<>();
        newTagNames.add("测试");
        newTagNames.add("test");
        blogService.reArrangeBlogTag(46, newTagNames);
    }
}
