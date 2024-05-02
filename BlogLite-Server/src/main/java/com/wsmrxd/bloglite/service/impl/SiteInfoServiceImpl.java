package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.SiteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wsmrxd.bloglite.enums.RedisKeyForHash.*;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CacheService cacheService;

    private static final int RANK_SIZE = 10;

    @Override
    public SiteInfo getSiteInfo() {
        SiteInfo ret = new SiteInfo();
        ret.setTotalBlogs(this.getTotalBlogsAsCached());
        ret.setTotalViews(this.getTotalViewsAsCached());
        return ret;
    }

    @Override
    @Cacheable("BlogRanking")
    public List<BlogPreview> getBlogRanking() {
        return blogMapper.selectBlogsOrderByViews(RANK_SIZE);
    }

    @Override
    @CacheEvict("BlogRanking")
    public void UpdateSiteInfo() {
        this.flushSiteInfo();
        /* 重建缓存 */
        this.getTotalBlogsAsCached();
        this.getTotalViewsAsCached();
    }

    @Override
    public Integer getTotalBlogsAsCached() {
        Integer ret = cacheService.hash().getValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalBlogs.name());
        if (ret == null) {
            ret = blogMapper.selectBlogCount();
            cacheService.hash().putKeyValToHash(Integer_SiteInfo.name(), SiteInfo_TotalBlogs.name(), ret);
        }
        return ret;
    }

    @Override
    public Integer getTotalViewsAsCached() {
        Integer ret = cacheService.hash().getValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalViews.name());
        if (ret == null) {
            ret = blogMapper.selectViewsCount();
            if (ret == null)
                ret = 0;
            cacheService.hash().putKeyValToHash(Integer_SiteInfo.name(), SiteInfo_TotalViews.name(), ret);
        }
        return ret;
    }

    @Override
    public void flushSiteInfo() {
        updateBlogViewsFromCache();
        cacheService.delete(Integer_SiteInfo.name());
    }

    @Override
    public void increaseBlogViews(int blogID) {
        cacheService.hash().increaseValueByHashKey(Integer_BlogViewsByID.name(), Integer.toString(blogID), 1);
        cacheService.hash().increaseValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalViews.name(), 1);
        cacheService.hash().increaseValueByHashKey(Integer_AddBlogViewsByID.name(), Integer.toString(blogID), 1);
    }

    private void updateBlogViewsFromCache() {
        Map<String, Integer> viewsMap = cacheService.hash().getHashEntriesByKey(Integer_AddBlogViewsByID.name());
        if (viewsMap != null) {
            var keySet = viewsMap.keySet();
            for (String key : keySet) {
                Integer addNum = viewsMap.get(key);
                blogMapper.updateBlogViewsByID(Integer.parseInt(key), addNum);
            }
        }
        cacheService.delete(Integer_AddBlogViewsByID.name());
    }
}
