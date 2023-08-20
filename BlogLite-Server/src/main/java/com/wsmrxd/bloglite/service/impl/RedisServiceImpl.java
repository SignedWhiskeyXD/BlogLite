package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    private BlogMapper blogMapper;

    @Autowired
    public void setRedisTemplate(@Qualifier("redisJsonTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }


    @Override
    public int getBlogViewsAsCached(int blogID) {
        var redisHashOps = redisTemplate.opsForHash();
        Integer ret = (Integer) redisHashOps.get(blogViewsKey, Integer.toString(blogID));
        if(ret != null) return ret;

        ret = blogMapper.selectViewsByBlogID(blogID);
        redisHashOps.put(blogViewsKey, Integer.toString(blogID), ret);
        return ret;
    }

    @Override
    public void increaseBlogViews(int blogID) {
        var redisHashOps = redisTemplate.opsForHash();

        if(redisHashOps.hasKey(blogViewsKey, Integer.toString(blogID)))
            redisHashOps.increment(blogViewsKey, Integer.toString(blogID), 1);

        if(redisHashOps.hasKey(siteInfoKey, totalViewsKey))
            redisHashOps.increment(siteInfoKey, totalViewsKey, 1);

        redisHashOps.increment(blogAddViewsKey, Integer.toString(blogID), 1);
    }

    @Override
    public BlogDetail getBlogDetail(int blogID) {
        var redisValOps = redisTemplate.opsForValue();
        final String redisKey = blogDetailPrefix + blogID;

        return (BlogDetail) redisValOps.get(redisKey);
    }

    @Override
    public void setBlogDetail(int blogID, BlogDetail toCache) {
        var redisValOps = redisTemplate.opsForValue();
        final String redisKey = blogDetailPrefix + blogID;

        redisValOps.set(redisKey, toCache);
    }

    @Override
    public PageInfo<BlogPreview> getBlogPreviewPage(int pageNum, int pageSize) {
        var redisHashOps = redisTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        return (PageInfo<BlogPreview>) redisHashOps.get(blogPreviewPageKey, hashKey);
    }

    @Override
    public void setBlogPreviewPage(int pageNum, int pageSize, PageInfo<BlogPreview> toCache) {
        var redisHashOps = redisTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        redisHashOps.put(blogPreviewPageKey, hashKey, toCache);
    }

    @Override
    public void flushBlogCache(int blogID) {
        redisTemplate.delete(blogAdminDetailPrefix + blogID);
        redisTemplate.delete(blogDetailPrefix + blogID);
        redisTemplate.delete(blogCardPrefix + blogID);
    }

    @Override
    public void flushBlogPagingCache() {
        redisTemplate.delete(blogPreviewPageKey);
    }

    @Override
    public BlogCard getBlogCard(int blogID) {
        var redisValOps = redisTemplate.opsForValue();
        final String redisKey = blogCardPrefix + blogID;

        return (BlogCard) redisValOps.get(redisKey);
    }

    @Override
    public void setBlogCard(int blogID, BlogCard toCache) {
        var redisValOps = redisTemplate.opsForValue();
        final String redisKey = blogCardPrefix + blogID;

        redisValOps.set(redisKey, toCache);
    }

    @Override
    public Integer getTotalBlogsAsCached() {
        var redisHashOps = redisTemplate.opsForHash();
        Integer ret = (Integer) redisHashOps.get(siteInfoKey, totalBlogsKey);
        if(ret == null){
            ret = blogMapper.selectBlogCount();
            redisHashOps.put(siteInfoKey, totalBlogsKey, ret);
        }
        return ret;
    }

    @Override
    public Integer getTotalViewsAsCached() {
        var redisHashOps = redisTemplate.opsForHash();
        Integer ret = (Integer) redisHashOps.get(siteInfoKey, totalViewsKey);
        if(ret == null){
            ret = blogMapper.selectViewsCount();
            if(ret == null)
                ret = 0;
            redisHashOps.put(siteInfoKey, totalViewsKey, ret);
        }
        return ret;
    }

    @Override
    public void flushSiteInfo() {
        updateBlogViewsFromCache();
        redisTemplate.delete(siteInfoKey);
    }

    private void updateBlogViewsFromCache(){
        var redisHashOps = redisTemplate.opsForHash();
        var viewsMap = redisHashOps.entries(blogAddViewsKey);
        var keySet = viewsMap.keySet();
        for(var key : keySet){
            String blogIDString = (String) key;
            Integer addNum = (Integer) viewsMap.get(key);
            blogMapper.updateBlogViewsByID(Integer.parseInt(blogIDString), addNum);
        }
        redisTemplate.delete(blogAddViewsKey);
    }
}
