package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogstream")
public class BlogStreamAPI {

    private BlogStreamService blogStreamService;

    @Autowired
    public void setBlogStreamService(BlogStreamService blogStreamService) {
        this.blogStreamService = blogStreamService;
    }

    @GetMapping("/init")
    public RestResponse serveBlogStreamInitiation(@RequestParam int initNum){
        return RestResponse.ok(blogStreamService.getInitStream(initNum));
    }

    @GetMapping
    public RestResponse serveBlogStream(@RequestParam(defaultValue = "1919810") int startID,
                                        @RequestParam(defaultValue = "5") int num){
        var ret = blogStreamService.getBlogStream(startID, num);
        return RestResponse.ok(ret);
    }
}
