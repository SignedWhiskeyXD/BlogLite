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

    @PostMapping
    public RestResponse modifyBlog(@RequestBody BlogModifyInfo modifyInfo){
        boolean result1 = false, result2 = false, result3 = false;
        if(modifyInfo.getNewTitle() != null && modifyInfo.getNewTitle().length() > 0)
            result1 = blogService.renameBlogTitle(modifyInfo.getId(), modifyInfo.getNewTitle());
        if(modifyInfo.getNewContent() != null && modifyInfo.getNewContent().length() > 0)
            result2 = blogService.editBlogContent(modifyInfo.getId(), modifyInfo.getNewContent());
        if(modifyInfo.getAddLikes() > 0)
            result3 = blogService.addBlogLikes(modifyInfo.getId(), modifyInfo.getAddLikes());

        return (result1 | result2 | result3) ? RestResponse.ok(null)
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
