package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.vo.*;

import java.util.List;

public interface RedisService {

    String allBlogIDsKey = "AllBlogIDs";

    String blogDetailPrefix = "BlogDetail_";

    String blogCardPrefix = "BlogCard_";

    String blogViewsKey = "BlogViews";

    String blogAddViewsKey = "BlogAddViews";

    String blogPreviewPageKey = "BlogPreviewPageInfo";

    String siteInfoKey = "SiteInfo";

    String totalBlogsKey = "totalBlogs";

    String totalViewsKey = "totalViews";

    int getBlogViewsAsCached(int blogID);

    void increaseBlogViews(int blogID);

    BlogDetail getBlogDetail(int blogID);

    void setBlogDetail(int blogID, BlogDetail toCache);

    PageInfo<BlogPreview> getBlogPreviewPage(int pageNum, int pageSize);

    void setBlogPreviewPage(int pageNum, int pageSize, PageInfo<BlogPreview> toCache);

    void flushBlogCache(int blogID);

    void flushBlogPagingCache();

    BlogCard getBlogCard(int blogID);

    void setBlogCard(int blogID, BlogCard toCache);

    Integer getTotalBlogsAsCached();

    Integer getTotalViewsAsCached();

    void flushSiteInfo();

    List<Integer> getBlogIDsStartAt(int startID, int blogNum);

    void addBlogIDtoZSet(int blogID);

    void removeBlogIDFromZSet(int blogID);
}
