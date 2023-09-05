package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.BlogCollectionService;
import com.wsmrxd.bloglite.vo.BlogCollectionVO;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collection")
public class BlogCollectionAPI {

    BlogCollectionService blogCollectionService;

    @Autowired
    public void setBlogCollectionService(BlogCollectionService blogCollectionService) {
        this.blogCollectionService = blogCollectionService;
    }

    @GetMapping("/all")
    RestResponse<List<BlogCollectionVO>> serveAllBlogCollections(){
        return RestResponse.ok(blogCollectionService.getAllBlogCollectionWithStatistic());
    }
}
