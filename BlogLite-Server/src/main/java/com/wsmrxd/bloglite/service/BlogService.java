package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPageView;
import com.wsmrxd.bloglite.vo.BlogAdminView;

import java.util.List;

public interface BlogService {
    Blog getBlogByID(int id);

    BlogAdminView getBlogViewByID(int id);

    BlogDetail getBlogDetail(int id);

    PageInfo<BlogPageView> getAllBlogsByPage(int pageNum, int pageSize);

    List<BlogTag> getAllTagsByBlogID(int blogID);

    int addNewBlog(BlogUploadInfo newBlog);

    void reArrangeBlogTag(int blogID, List<String> tagNames);

    boolean renameBlogTitle(int id, String newTitle);

    boolean editBlogAbstract(int id, String newAbstract);

    boolean editBlogContent(int id, String newContent);

    boolean addBlogViews(int id, int moreViews);

    boolean deleteBlog(int id);

    void flushBlogCache(int blogID);

    void flushBlogPagingCache();
}
