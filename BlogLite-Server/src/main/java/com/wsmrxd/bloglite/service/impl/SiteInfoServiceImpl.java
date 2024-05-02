package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

    private final BlogMapper blogMapper;

    private final ConcurrentHashMap<Integer, AtomicInteger> blogViews;

    private final AtomicInteger totalBlogs;

    private final AtomicInteger totalViews;

    private static final int RANK_SIZE = 10;

    @Autowired
    public SiteInfoServiceImpl(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;

        this.blogViews = new ConcurrentHashMap<>();

        /* 加载文章总数 */
        int totalBlogs = blogMapper.selectBlogCount();
        this.totalBlogs = new AtomicInteger(totalBlogs);

        /* 加载全站访问量 */
        int totalViews = blogMapper.selectViewsCount();
        this.totalViews = new AtomicInteger(totalViews);
    }

    @Override
    @Cacheable("BlogRanking")
    public List<BlogPreview> getBlogRanking() {
        return blogMapper.selectBlogsOrderByViews(RANK_SIZE);
    }

    @Override
    public int getTotalBlogsAsCached() {
        return totalBlogs.get();
    }

    @Override
    public int getTotalViewsAsCached() {
        return totalViews.get();
    }

    @Override
    public int getBlogLiveViews(int blogID) {
        if (blogViews.containsKey(blogID)) return blogViews.get(blogID).get();

        int views = blogMapper.selectViewsByBlogID(blogID);
        blogViews.put(blogID, new AtomicInteger(views));
        return views;
    }

    @Override
    public synchronized boolean flushSiteInfo() {
        if (blogViews.isEmpty()) return false;

        for (Integer blogId : blogViews.keySet()) {
            int views = blogViews.get(blogId).get();
            blogMapper.updateBlogViewsByID(blogId, views);
        }
        blogViews.clear();
        return true;
    }

    @Override
    public void increaseBlogViews(int blogID) {
        totalViews.incrementAndGet();

        /* 已载入缓存，直接自增 */
        AtomicInteger viewsFromMap = blogViews.get(blogID);
        if (viewsFromMap != null) {
            viewsFromMap.incrementAndGet();
            return;
        }

        /* 可能是新文章，没有进缓存，从数据库拿 */
        Integer views = blogMapper.selectViewsByBlogID(blogID);
        if (views == null) return;

        blogViews.put(blogID, new AtomicInteger(views + 1));
    }

    @Override
    public void modifyTotalBlogs(int delta) {
        if (delta > 0)
            totalBlogs.incrementAndGet();
        else if (delta < 0)
            totalBlogs.decrementAndGet();
    }
}
