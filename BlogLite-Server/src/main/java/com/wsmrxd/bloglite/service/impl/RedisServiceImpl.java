package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public void flushBlogCache(int blogID) {
        redisTemplate.delete(blogDetailPrefix + blogID);
        redisTemplate.delete(blogCardPrefix + blogID);
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

    @Override
    public List<Integer> getBlogIDsStartAt(int startID, int blogNum) {
        if(Boolean.FALSE.equals(redisTemplate.hasKey(allBlogIDsKey)))
            cacheAllBlogIDs();
        var redisZSetOps = redisTemplate.opsForZSet();

        var idSet = redisZSetOps
                .reverseRangeByScore(allBlogIDsKey, Double.NEGATIVE_INFINITY, startID, 0, blogNum);

        if (idSet == null) return null;
        List<Integer> ret = new ArrayList<>(idSet.size());
        for(Object id : idSet)
            ret.add((Integer) id);

        return ret;
    }

    @Override
    public void addBlogIDtoZSet(int blogID) {
        if(Boolean.FALSE.equals(redisTemplate.hasKey(allBlogIDsKey)))
            cacheAllBlogIDs();
        var redisZSetOps = redisTemplate.opsForZSet();

        redisZSetOps.add(allBlogIDsKey, blogID, blogID);
    }

    @Override
    public void removeBlogIDFromZSet(int blogID) {
        if(Boolean.FALSE.equals(redisTemplate.hasKey(allBlogIDsKey)))
            cacheAllBlogIDs();
        var redisZSetOps = redisTemplate.opsForZSet();

        redisZSetOps.remove(allBlogIDsKey, blogID);
    }

    private void cacheAllBlogIDs(){
        redisTemplate.delete(allBlogIDsKey);
        var redisZSetOps = redisTemplate.opsForZSet();

        List<Integer> allBlogIDs = blogMapper.selectAllBlogID();
        for(Integer id : allBlogIDs)
            redisZSetOps.add(allBlogIDsKey, id, id);
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
