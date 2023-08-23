package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;

import java.util.List;

public interface BlogService {

    BlogAdminDetail getBlogAdminDetailByID(int id);

    BlogDetail getBlogDetail(int id);

    BlogCard getBlogCard(int blogID);

    PageInfo<BlogPreview> getAllBlogsByPage(int pageNum, int pageSize);

    int addNewBlog(BlogUploadInfo newBlog);

    void modifyBlog(int id, BlogUploadInfo modifyInfo);

    boolean deleteBlog(int id);

    int getBlogViewsAsCached(int blogID);

    void flushSiteInfo();

    Integer getTotalBlogsAsCached();

    Integer getTotalViewsAsCached();

    List<Integer> getBlogIDsStartAt(int startID, int blogNum);

    List<Integer> getBlogIDsByCollectionIDAsCached(int collectionID);

    void increaseBlogViews(int blogID);
}
