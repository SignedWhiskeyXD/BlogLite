package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.BlogSearchService;
import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class BlogSearchAPI {

    @Autowired
    private BlogStreamService blogStreamService;

    @Autowired
    private BlogSearchService blogSearchService;

    @GetMapping
    public RestResponse<List<BlogCard>> serveBlogsBySearchingKeyword(@RequestParam("keyword") String keyword){
        List<Integer> blogIDFound = blogSearchService.searchBlogByKeyword(keyword);
        List<BlogCard> ret = blogStreamService.getBlogCardList(blogIDFound);
        return RestResponse.ok(ret);
    }
}
