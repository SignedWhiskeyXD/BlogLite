package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.dto.BlogModifyInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.service.BlogService;
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
        return result ? RestResponse.ok(null)
                : RestResponse.build(400, "Bad Request");
    }

    @PutMapping
    public RestResponse addNewBlog(@RequestBody BlogUploadInfo newBlog){
        boolean result =  blogService.addNewBlog(newBlog) > 0;
        return result ? RestResponse.ok(null)
                : RestResponse.build(400, "Bad Request");
    }

    @DeleteMapping("/{id}")
    public RestResponse deleteBlog(@PathVariable int id){
        return blogService.deleteBlog(id)
                ? RestResponse.ok(null)
                : RestResponse.build(400, "Bad Request");
    }
}
