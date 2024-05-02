package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.SiteInfo;

import java.util.List;

public interface SiteInfoService {

    SiteInfo getSiteInfo();

    List<BlogPreview> getBlogRanking();

    void UpdateSiteInfo();

    Integer getTotalBlogsAsCached();

    Integer getTotalViewsAsCached();

    void flushSiteInfo();

    void increaseBlogViews(int blogID);
}
