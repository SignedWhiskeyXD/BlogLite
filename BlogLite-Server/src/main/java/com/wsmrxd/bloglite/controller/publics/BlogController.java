package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.impl.BlogService;
import com.wsmrxd.bloglite.vo.BlogView;
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
        var tags = blogService.getAllTagsByBlogID(id);

        return RestResponse.ok(new BlogView(blog, tags));
    }

    @GetMapping
    public RestResponse getBlogsByPage(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize){
        return RestResponse.ok(blogService.getAllBlogsByPage(pageNum, pageSize));
    }

    @GetMapping("/gettags/{blogID}")
    public RestResponse getTagsByBlogID(@PathVariable int blogID){
        return RestResponse.ok(blogService.getAllTagsByBlogID(blogID));
    }
}
