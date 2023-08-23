package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
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

    @GetMapping("/{id}")
    public RestResponse serveBlogByID(@PathVariable int id){
        var blogAdminDetail = blogService.getBlogAdminDetailByID(id);
        if(blogAdminDetail == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");

        return RestResponse.ok(blogAdminDetail);
    }

    @GetMapping
    public RestResponse getBlogsByPage(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize){
        return RestResponse.ok(blogService.getAllBlogsByPage(pageNum, pageSize));
    }

    @PostMapping("/{id}")
    public RestResponse modifyBlog(@RequestBody BlogUploadInfo modifyInfo, @PathVariable int id){
        blogService.modifyBlog(id, modifyInfo);
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
