package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;

public interface BlogService {

    BlogAdminDetail getBlogAdminDetailByID(int id);

    BlogDetail getBlogDetail(int id);

    PageInfo<BlogPreview> getAllBlogsByPage(int pageNum, int pageSize);

    int addNewBlog(BlogUploadInfo newBlog);

    void modifyBlog(int id, BlogUploadInfo modifyInfo);

    boolean deleteBlog(int id);

    int getBlogViewsAsCached(int blogID);

    void flushSiteInfo();
}
