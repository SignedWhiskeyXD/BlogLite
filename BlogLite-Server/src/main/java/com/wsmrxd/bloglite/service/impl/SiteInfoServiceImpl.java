package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.SiteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

    @Autowired
    private BlogService blogService;

    @Override
    public SiteInfo getSiteInfo() {
        SiteInfo ret = new SiteInfo();
        ret.setTotalBlogs(blogService.getTotalBlogsAsCached());
        ret.setTotalViews(blogService.getTotalViewsAsCached());
        return ret;
    }

    @Override
    @Scheduled(fixedRate = 300000)      // 每五分钟向数据库更新一次浏览次数
    public void UpdateSiteInfo() {
        blogService.flushSiteInfo();
        blogService.getTotalBlogsAsCached();
        blogService.getTotalViewsAsCached();
    }
}
