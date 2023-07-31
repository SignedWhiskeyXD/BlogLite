package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/{id}")
    public RestResponse serveBlogByID(@PathVariable int id){
        var blog = blogService.getBlogByID(id);
        if(blog == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");
        return RestResponse.ok(blog);
    }

    @GetMapping
    public RestResponse getUsersByPage(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize){
        return RestResponse.ok(blogService.getAllBlogsByPage(pageNum, pageSize));
    }


}
