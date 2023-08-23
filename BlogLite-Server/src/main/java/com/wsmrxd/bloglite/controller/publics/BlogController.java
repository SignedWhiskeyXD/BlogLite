package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.vo.BlogDetail;
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
        BlogDetail ret = blogService.getBlogDetail(id);
        if(ret == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");
        return RestResponse.ok(ret);
    }
}
