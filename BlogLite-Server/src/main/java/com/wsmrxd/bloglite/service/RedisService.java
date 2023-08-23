package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.BlogCard;

import java.util.List;

public interface RedisService {

    int getBlogViewsAsCached(int blogID);

    BlogCard getBlogCard(int blogID);

    void setBlogCard(int blogID, BlogCard toCache);

    Integer getTotalBlogsAsCached();

    Integer getTotalViewsAsCached();

    void flushSiteInfo();

    List<Integer> getBlogIDsStartAt(int startID, int blogNum);

}
