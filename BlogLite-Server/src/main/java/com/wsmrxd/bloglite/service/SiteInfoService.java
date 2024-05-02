package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.BlogPreview;

import java.util.List;

public interface SiteInfoService {

    /**
     * @return 全站文章访问量排行榜
     */
    List<BlogPreview> getBlogRanking();

    /**
     * @return 全站文章总数
     */
    int getTotalBlogsAsCached();

    /**
     * @return 全站访问量
     */
    int getTotalViewsAsCached();

    /**
     * @param blogID    文章ID
     * @return          文章实时访问量
     */
    int getBlogLiveViews(int blogID);

    /**
     * 清空站点信息缓存，并将缓存中的站点信息同步至数据库
     * @return  true    成功将缓存中的数据同步至数据库
     *          false   无需同步
     */
    boolean flushSiteInfo();

    /**
     * 将ID对应的文章访问量自增1，同时全站访问量自增1
     * @param blogID 文章ID
     */
    void increaseBlogViews(int blogID);


    /**
     * 改变全站文章数
     * @param delta 总文章数自增或自减
     */
    void modifyTotalBlogs(int delta);
}
