package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.dto.BlogCollectionCreateInfo;
import com.wsmrxd.bloglite.entity.BlogCollection;
import com.wsmrxd.bloglite.service.BlogCollectionService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/collection")
public class BlogCollectionAdminAPI {

    private BlogCollectionService service;

    @Autowired
    public void setService(BlogCollectionService service) {
        this.service = service;
    }

    @PostMapping
    public RestResponse modifyCollectionInfo(@RequestBody BlogCollection modifyInfo){
        service.modifyCollectionInfo(modifyInfo);
        return RestResponse.ok(null);
    }

    @PutMapping
    public RestResponse createNewCollection(@RequestBody BlogCollectionCreateInfo newCollection){
        service.createNewCollection(newCollection);
        return RestResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public RestResponse deleteBlogCollection(@PathVariable Integer id){
        service.removeBlogCollection(id);
        return RestResponse.ok(null);
    }
}
