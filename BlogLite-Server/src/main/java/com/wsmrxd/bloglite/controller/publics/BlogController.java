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
        var blogView = blogService.getBlogViewByID(id);
        if(blogView == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");

        return RestResponse.ok(blogView);
    }

    @GetMapping
    public RestResponse getBlogsByPage(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize){
        return RestResponse.ok(blogService.getAllBlogsByPage(pageNum, pageSize));
    }

    // TODO: 没什么用。等会删掉算了
    @GetMapping("/gettags/{blogID}")
    public RestResponse getTagsByBlogID(@PathVariable int blogID){
        return RestResponse.ok(blogService.getAllTagsByBlogID(blogID));
    }
}
