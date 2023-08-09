package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.BlogTagService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/allTags")
public class BlogTagAPI {

    private BlogTagService blogTagService;

    @Autowired
    public void setBlogTagService(BlogTagService blogTagService) {
        this.blogTagService = blogTagService;
    }

    @GetMapping
    public RestResponse serveAllTags(){
        return RestResponse.ok(blogTagService.getAllTags());
    }
}
