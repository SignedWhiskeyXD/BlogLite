package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;

import java.util.List;

public interface RedisService {

    String blogAdminDetailPrefix = "BlogAdminDetail_";

    String blogDetailPrefix = "BlogDetail_";

    String blogCardPrefix = "BlogCard_";

    String blogTagPrefix = "BlogTag_";

    String blogViewsKey = "BlogViews";

    String blogPreviewPageKey = "BlogPreviewPageInfo";

    String blogTagPageKey = "TagPageInfo";

    int getBlogViewsAsCached(int blogID);

    void increaseBlogViews(int blogID);

    BlogAdminDetail getBlogAdminDetail(int blogID);

    void setBlogAdminDetail(int blogID, BlogAdminDetail toCache);

    BlogDetail getBlogDetail(int blogID);

    void setBlogDetail(int blogID, BlogDetail toCache);

    PageInfo<BlogPreview> getBlogPreviewPage(int pageNum, int pageSize);

    void setBlogPreviewPage(int pageNum, int pageSize, PageInfo<BlogPreview> toCache);

    void flushBlogCache(int blogID);

    void flushBlogPagingCache();

    BlogCard getBlogCard(int blogID);

    void setBlogCard(int blogID, BlogCard toCache);

    BlogTag getBlogTag(int tagID);

    void setBlogTag(BlogTag blogTag);

    // TODO: 考虑使用Redis集合实现
    List<BlogTag> getAllBlogTags();

    void setAllBlogTags(List<BlogTag> allTags);

    PageInfo<BlogTag> getTagPageInfo(int pageNum, int pageSize);

    void setTagPageInfo(int pageNum, int pageSize, PageInfo<BlogTag> pageInfo);

    void flushTagPageInfoCache();

    void flushTagCache(int tagID);
}
