package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogStream;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogstream")
public class BlogStreamAPI {

    private BlogStreamService blogStreamService;

    @Autowired
    public void setBlogStreamService(BlogStreamService blogStreamService) {
        this.blogStreamService = blogStreamService;
    }

    @GetMapping("/init")
    public RestResponse<BlogStream> serveBlogStreamInitiation(@RequestParam int initNum){
        return RestResponse.ok(blogStreamService.getInitStream(initNum));
    }

    @GetMapping
    public RestResponse<BlogStream> serveBlogStream(@RequestParam(defaultValue = "1919810") int startID,
                                        @RequestParam(defaultValue = "5") int num){
        var ret = blogStreamService.getBlogStream(startID, num);
        return RestResponse.ok(ret);
    }

    @GetMapping("/collection/{collectionID}")
    public RestResponse<List<BlogCard>> serveBlogStreamByCollectionID(@PathVariable Integer collectionID){
        List<BlogCard> ret = blogStreamService.getAllBlogsFromCollection(collectionID);
        return RestResponse.ok(ret);
    }
}
