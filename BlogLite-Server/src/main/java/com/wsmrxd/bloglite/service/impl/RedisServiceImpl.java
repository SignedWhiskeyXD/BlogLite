package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.cache.CacheService;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wsmrxd.bloglite.enums.RedisKeyForHash.*;
import static com.wsmrxd.bloglite.enums.RedisKeyForZSet.*;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public int getBlogViewsAsCached(int blogID) {
        Integer ret = cacheService.getValueByHashKey(Integer_BlogViewsByID.name(), Integer.toString(blogID));
        if(ret != null) return ret;

        ret = blogMapper.selectViewsByBlogID(blogID);
        cacheService.putKeyValToHash(Integer_BlogViewsByID.name(), Integer.toString(blogID), ret);
        return ret;
    }

    @Override
    public BlogCard getBlogCard(int blogID) {
        return cacheService.getZSetValueByScore(BlogCard_ByID.name(), blogID);
    }

    @Override
    public void setBlogCard(int blogID, BlogCard toCache) {
        cacheService.addValueToZSet(BlogCard_ByID.name(), toCache, blogID);
    }

    @Override
    public Integer getTotalBlogsAsCached() {
        Integer ret = cacheService.getValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalBlogs.name());
        if(ret == null){
            ret = blogMapper.selectBlogCount();
            cacheService.putKeyValToHash(Integer_SiteInfo.name(), SiteInfo_TotalBlogs.name(), ret);
        }
        return ret;
    }

    @Override
    public Integer getTotalViewsAsCached() {
        Integer ret = cacheService.getValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalViews.name());
        if(ret == null){
            ret = blogMapper.selectViewsCount();
            if(ret == null)
                ret = 0;
            cacheService.putKeyValToHash(Integer_SiteInfo.name(), SiteInfo_TotalViews.name(), ret);
        }
        return ret;
    }

    @Override
    public void flushSiteInfo() {
        updateBlogViewsFromCache();
        cacheService.delete(Integer_SiteInfo.name());
    }

    @Override
    public List<Integer> getBlogIDsStartAt(int startID, int blogNum) {
        /*if(Boolean.FALSE.equals(redisTemplate.hasKey(allBlogIDsKey)))
            cacheAllBlogIDs();
        var redisZSetOps = redisTemplate.opsForZSet();

        var idSet = redisZSetOps
                .reverseRangeByScore(allBlogIDsKey, Double.NEGATIVE_INFINITY, startID, 0, blogNum);

        if (idSet == null) return null;
        List<Integer> ret = new ArrayList<>(idSet.size());
        for(Object id : idSet)
            ret.add((Integer) id);

        return ret;*/

        if(!cacheService.hasKey(Integer_AllBlogIDs.name()))
            cacheAllBlogIDs();

        List<Integer> blogIDs = cacheService.getListByReversedScoreRange(
                Integer_AllBlogIDs.name(), Double.NEGATIVE_INFINITY, startID, 0, blogNum);

        if(blogIDs != null)
            return blogIDs;
        else return new ArrayList<>();

    }

    private void cacheAllBlogIDs(){
        cacheService.delete(Integer_AllBlogIDs.name());

        List<Integer> allBlogIDs = blogMapper.selectAllBlogID();
        for(Integer id : allBlogIDs)
            cacheService.addValueToZSet(Integer_AllBlogIDs.name(), id, id);
    }

    private void updateBlogViewsFromCache(){
        Map<Integer, Integer> viewsMap = cacheService.getHashEntriesByKey(Integer_AddBlogViewsByID.name());
        var keySet = viewsMap.keySet();
        for(Integer key : keySet){
            Integer addNum = viewsMap.get(key);
            blogMapper.updateBlogViewsByID(key, addNum);
        }
        cacheService.delete(Integer_AddBlogViewsByID.name());
    }
}
