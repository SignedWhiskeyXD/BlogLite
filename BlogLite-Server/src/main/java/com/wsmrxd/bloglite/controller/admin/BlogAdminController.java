package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.impl.BlogService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/blog")
public class BlogAdminController {

    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/{id}")
    public RestResponse modifyBlog(@RequestBody BlogUploadInfo modifyInfo, @PathVariable int id){
        boolean result = blogService.renameBlogTitle(id, modifyInfo.getTitle());
        result &= blogService.editBlogContent(id, modifyInfo.getContent());
        blogService.reArrangeBlogTag(id, modifyInfo.getTagNames());
        if(!result)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot Modify The Blog");
        return RestResponse.ok(null);
    }

    @PutMapping
    public RestResponse addNewBlog(@RequestBody BlogUploadInfo newBlog){
        boolean result =  blogService.addNewBlog(newBlog) > 0;
        if(!result)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot Add The Blog");
        return RestResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public RestResponse deleteBlog(@PathVariable int id){
        boolean result = blogService.deleteBlog(id);
        if(!result)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot Delete The Blog");
        return RestResponse.ok(null);
    }
}
