package com.wsmrxd.bloglite.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.BlogTagService;
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/blog")
public class BlogAdminAPI {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogTagService blogTagService;

    @GetMapping("/{id}")
    public RestResponse<BlogAdminDetail> serveBlogByID(@PathVariable int id){
        var blogAdminDetail = blogService.getBlogAdminDetailByID(id);
        if(blogAdminDetail == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");

        return RestResponse.ok(blogAdminDetail);
    }

    @GetMapping
    public RestResponse<PageInfo<BlogPreview>> getBlogsByPage(@RequestParam(defaultValue = "1") int pageNum,
                                                              @RequestParam(defaultValue = "10") int pageSize){
        return RestResponse.ok(blogService.getAllBlogsByPage(pageNum, pageSize));
    }

    @PostMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<Object> modifyBlog(@RequestBody BlogUploadInfo modifyInfo, @PathVariable int id){
        BlogAdminDetail original = blogService.getBlogAdminDetailByID(id);
        if(original.getBlog().isModified(modifyInfo)) {
            log.trace("修改文章{}内容...", id);
            blogService.modifyBlog(id, modifyInfo);
        }

        if(!modifyInfo.getTagNames().equals(original.getTagNames())) {
            log.trace("修改文章{}标签列表...", id);
            blogService.reArrangeBlogTag(id, modifyInfo.getTagNames());
        }

        if(!modifyInfo.getCollections().equals(original.getCollections())) {
            log.trace("修改文章{}合集列表...", id);
            blogService.reArrangeBlogCollection(id, modifyInfo.getCollections());
        }

        return RestResponse.ok();
    }

    @PutMapping
    public RestResponse<Object> addNewBlog(@RequestBody BlogUploadInfo newBlog){
        int newBlogID = blogService.addNewBlog(newBlog);
        if(newBlogID < 1)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot Add The Blog");

        // 这只是个标志，让缓存自动配置进行刷新
        if(!newBlog.getTagNames().isEmpty())
            blogTagService.addTag(null);

        return RestResponse.ok();
    }

    @DeleteMapping("/{id}")
    public RestResponse<Object> deleteBlog(@PathVariable int id){
        boolean result = blogService.deleteBlog(id);
        if(!result)
            throw new BlogException(ErrorCode.BAD_REQUEST, "Cannot Delete The Blog");
        return RestResponse.ok();
    }
}
