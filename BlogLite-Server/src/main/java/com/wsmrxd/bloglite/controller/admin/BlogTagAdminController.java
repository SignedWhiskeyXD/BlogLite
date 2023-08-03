package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.BlogTagService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/blogtag")
public class BlogTagAdminController {
    private BlogTagService tagService;

    @Autowired
    public void setTagService(BlogTagService tagService) {
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
    public RestResponse getTagsByPage(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        return RestResponse.ok(tagService.getAllTagsByPage(pageNum, pageSize));
    }

    @PostMapping
    public RestResponse renameTagByID(@RequestParam int renamedID,
                                      @RequestParam String newName){
        boolean result = tagService.renameTag(renamedID, newName);
        if(!result)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot rename the tag");
        return RestResponse.ok(null);
    }

    @PutMapping
    public RestResponse addTag(@RequestParam String tagName){
        int newTagID = tagService.addTag(tagName);
        if(newTagID < 1)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot add this tag");
        return RestResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public RestResponse deleteBlogTagByID(@PathVariable int id){
        boolean result = tagService.removeTag(id);
        if(!result)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot delete the tag");
        return RestResponse.ok(null);
    }
}
