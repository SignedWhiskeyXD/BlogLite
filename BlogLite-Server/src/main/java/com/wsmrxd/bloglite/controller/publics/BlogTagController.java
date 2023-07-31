package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.base.BlogTagServiceBase;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogtag")
public class BlogTagController {
    private BlogTagServiceBase tagService;

    @Autowired
    public void setTagService(BlogTagServiceBase tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    public RestResponse serveBlogTagByID(@PathVariable Integer id){
        BlogTag ret = tagService.getTagByID(id);
        if(ret == null)
            throw new BlogException(ErrorCode.TAG_NOT_FOUND, "Blog Tag Not Found!");

        return RestResponse.ok(ret);
    }

    @GetMapping
    public RestResponse getUsersByPage(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        return RestResponse.ok(tagService.getAllTagsByPage(pageNum, pageSize));
    }

    @PostMapping
    public RestResponse renameTagByID(@RequestParam int renamedID,
                                      @RequestParam String newName){
        boolean result = tagService.renameTag(renamedID, newName);
        return result ? RestResponse.ok(null) : RestResponse.build(400, "Bad Request");
    }

    @PutMapping
    public RestResponse addTag(@RequestParam String tagName){
        boolean result = tagService.addTag(tagName);
        return result ? RestResponse.ok(null) : RestResponse.build(400, "Bad Request");
    }

    @DeleteMapping("/{id}")
    public RestResponse deleteBlogTagByID(@PathVariable int id){
        boolean result = tagService.removeTag(id);
        return result ? RestResponse.ok(null) : RestResponse.build(400, "Bad Request");
    }
}
