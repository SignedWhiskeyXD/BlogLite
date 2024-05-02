package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.BlogService;
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

    @Autowired
    private BlogStreamService blogStreamService;

    @Autowired
    private BlogService blogService;

    @Deprecated
    @GetMapping("/init")
    public RestResponse<BlogStream> serveBlogStreamInitiation(@RequestParam int initNum){
        return RestResponse.ok(blogStreamService.getInitStream(initNum));
    }

    @GetMapping
    public RestResponse<BlogStream> serveBlogStream(@RequestParam(required = false) Integer startID,
                                        @RequestParam(defaultValue = "5") int num){
        if (startID == null) return RestResponse.ok(blogStreamService.getInitStream(num));

        return RestResponse.ok(blogStreamService.getBlogStream(startID, num));
    }

    @GetMapping("/collection/{collectionID}")
    public RestResponse<List<BlogCard>> serveBlogsByCollectionID(@PathVariable Integer collectionID){
        List<Integer> blogIDs = blogService.getBlogIDsByCollectionIDAsCached(collectionID);
        List<BlogCard> ret = blogStreamService.getBlogCardList(blogIDs);
        return RestResponse.ok(ret);
    }
}
