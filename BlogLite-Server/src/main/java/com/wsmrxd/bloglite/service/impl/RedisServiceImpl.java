package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    private RedisTemplate<String, Object> jsonTemplate;

    private RedisTemplate<String, String> stringTemplate;

    private BlogMapper blogMapper;

    @Autowired
    public void setJsonTemplate(@Qualifier("redisJsonTemplate") RedisTemplate<String, Object> jsonTemplate) {
        this.jsonTemplate = jsonTemplate;
    }

    @Autowired
    public void setStringTemplate(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> stringTemplate) {
        this.stringTemplate = stringTemplate;
    }


    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Override
    public int getBlogViewsAsCached(int blogID) {
        var redisHashOps = jsonTemplate.opsForHash();
        Integer ret = (Integer) redisHashOps.get(blogViewsKey, Integer.toString(blogID));
        if(ret != null) return ret;

        ret = blogMapper.selectViewsByBlogID(blogID);
        redisHashOps.put(blogViewsKey, Integer.toString(blogID), ret);
        return ret;
    }

    @Override
    public void increaseBlogViews(int blogID) {
        var redisHashOps = jsonTemplate.opsForHash();
        if(redisHashOps.hasKey(blogViewsKey, Integer.toString(blogID)))
            redisHashOps.increment(blogViewsKey, Integer.toString(blogID), 1);
    }

    @Override
    public BlogAdminDetail getBlogAdminDetail(int blogID) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogAdminDetailPrefix + blogID;

        return (BlogAdminDetail) redisValOps.get(redisKey);
    }

    @Override
    public void setBlogAdminDetail(int blogID, BlogAdminDetail toCache) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogAdminDetailPrefix + blogID;

        redisValOps.set(redisKey, toCache);
    }

    @Override
    public BlogDetail getBlogDetail(int blogID) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogDetailPrefix + blogID;

        return (BlogDetail) redisValOps.get(redisKey);
    }

    @Override
    public void setBlogDetail(int blogID, BlogDetail toCache) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogDetailPrefix + blogID;

        redisValOps.set(redisKey, toCache);
    }

    @Override
    public PageInfo<BlogPreview> getBlogPreviewPage(int pageNum, int pageSize) {
        var redisHashOps = jsonTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        return (PageInfo<BlogPreview>) redisHashOps.get(blogPreviewPageKey, hashKey);
    }

    @Override
    public void setBlogPreviewPage(int pageNum, int pageSize, PageInfo<BlogPreview> toCache) {
        var redisHashOps = jsonTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        redisHashOps.put(blogPreviewPageKey, hashKey, toCache);
    }

    @Override
    public void flushBlogCache(int blogID) {
        jsonTemplate.delete(blogAdminDetailPrefix + blogID);
        jsonTemplate.delete(blogDetailPrefix + blogID);
        jsonTemplate.delete(blogCardPrefix + blogID);
    }

    @Override
    public void flushBlogPagingCache() {
        jsonTemplate.delete(blogPreviewPageKey);
    }

    @Override
    public BlogCard getBlogCard(int blogID) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogCardPrefix + blogID;

        return (BlogCard) redisValOps.get(redisKey);
    }

    @Override
    public void setBlogCard(int blogID, BlogCard toCache) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogCardPrefix + blogID;

        redisValOps.set(redisKey, toCache);
    }

    @Override
    public BlogTag getBlogTag(int tagID) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogTagPrefix + tagID;

        return (BlogTag) redisValOps.get(redisKey);
    }

    @Override
    public void setBlogTag(BlogTag blogTag) {
        var redisValOps = jsonTemplate.opsForValue();
        final String redisKey = blogTagPrefix + blogTag.getId();

        redisValOps.set(redisKey, blogTag);
    }

    @Override
    public List<BlogTag> getAllBlogTags() {
        var redisValOps = jsonTemplate.opsForValue();
        return (List<BlogTag>) redisValOps.get("AllBlogTags");
    }

    @Override
    public void setAllBlogTags(List<BlogTag> allTags) {
        jsonTemplate.opsForValue().set("AllBlogTags", allTags);
    }

    @Override
    public PageInfo<BlogTag> getTagPageInfo(int pageNum, int pageSize) {
        var redisHashOps = jsonTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        return (PageInfo<BlogTag>) redisHashOps.get(blogTagPageKey, hashKey);
    }

    @Override
    public void setTagPageInfo(int pageNum, int pageSize, PageInfo<BlogTag> pageInfo) {
        var redisHashOps = jsonTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        redisHashOps.put(blogTagPageKey, hashKey, pageInfo);
    }

    @Override
    public void flushTagPageInfoCache() {
        jsonTemplate.delete(blogTagPageKey);
    }

    @Override
    public void flushTagCache(int tagID) {
        jsonTemplate.delete(blogTagPrefix + tagID);
    }
}
