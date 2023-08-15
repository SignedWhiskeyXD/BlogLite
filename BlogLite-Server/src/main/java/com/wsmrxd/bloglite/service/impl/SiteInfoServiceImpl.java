package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.SiteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public SiteInfo getSiteInfo() {
        SiteInfo ret = new SiteInfo();
        ret.setTotalBlogs(redisService.getTotalBlogsAsCached());
        ret.setTotalViews(redisService.getTotalViewsAsCached());
        return ret;
    }

    @Override
    public void UpdateSiteInfo() {
        redisService.flushSiteInfo();
        redisService.getTotalBlogsAsCached();
        redisService.getTotalViewsAsCached();
    }
}
