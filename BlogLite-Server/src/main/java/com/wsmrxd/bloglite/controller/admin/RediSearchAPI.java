package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.service.impl.BlogSearchServiceRedisImpl;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/redisearch")
public class RediSearchAPI {

    @Autowired(required = false)
    private BlogSearchServiceRedisImpl rediSearch;

    @PostMapping
    public RestResponse<Object> prepareBlogIndex(@RequestParam("blogID") Integer blogID){
        if(rediSearch == null)
            return RestResponse.build(52400, "RediSearch Not Enabled!");

        if(rediSearch.addDocument(blogID))
            return RestResponse.ok();
        else
            return RestResponse.build(52400, "Cannot create index for Blog!");
    }

    @DeleteMapping
    public RestResponse<Object> deleteBlogIndex(@RequestParam("blogID") Integer blogID){
        if(rediSearch == null)
            return RestResponse.build(52400, "RediSearch Not Enabled!");

        if(rediSearch.deleteDocument(blogID))
            return RestResponse.ok();
        else
            return RestResponse.build(52400, "Cannot delete index for Blog!");
    }

    @PostMapping("/init")
    public RestResponse<Object> initRediSearch(){
        if(rediSearch == null)
            return RestResponse.build(52400, "RediSearch Not Enabled!");

        rediSearch.initBlogRediSearch();
        return RestResponse.ok();
    }
}
