package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.SiteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

    @Autowired
    private BlogService blogService;

    private static final int rankSize = 10;

    @Override
    public SiteInfo getSiteInfo() {
        SiteInfo ret = new SiteInfo();
        ret.setTotalBlogs(blogService.getTotalBlogsAsCached());
        ret.setTotalViews(blogService.getTotalViewsAsCached());
        return ret;
    }

    @Override
    @Cacheable("BlogRanking")
    public List<BlogPreview> getBlogRanking(){
        return blogService.getBlogRanking(rankSize);
    }

    @Override
    @CacheEvict("BlogRanking")
    public void UpdateSiteInfo() {
        blogService.flushSiteInfo();
        blogService.getTotalBlogsAsCached();
        blogService.getTotalViewsAsCached();
    }
}
