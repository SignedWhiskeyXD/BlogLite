package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogStream;
import com.wsmrxd.bloglite.vo.BlogCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogStreamServiceImpl implements BlogStreamService {

    private BlogMapper mapper;

    private RedisService redisService;

    @Autowired
    public void setMapper(BlogMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public BlogStream getInitStream(int num) {
        List<Integer> latestBlogIDList = mapper.selectLatestBlogIDs(1919810, num);
        return constructBlogStream(latestBlogIDList);
    }

    @Override
    public BlogStream getBlogStream(int startID, int num) {
        List<Integer> latestBlogIDList = mapper.selectLatestBlogIDs(startID, num);
        return constructBlogStream(latestBlogIDList);
    }

    private BlogStream constructBlogStream(List<Integer> latestBlogIDList) {
        var blogItems = new ArrayList<BlogCard>();

        for(int blogID : latestBlogIDList){
            var blogItem = getBlogCard(blogID);
            if(blogItem != null)
                blogItems.add(blogItem);
        }

        var initBlogStream = new BlogStream();
        initBlogStream.setBlogNum(blogItems.size());
        initBlogStream.setBlogList(blogItems);
        if(!latestBlogIDList.isEmpty())
            initBlogStream.setNextRequestParam(latestBlogIDList.get(latestBlogIDList.size() - 1));
        else
            initBlogStream.setNextRequestParam(null);

        return initBlogStream;
    }

    private BlogCard getBlogCard(int blogID){
        BlogCard blogCache = redisService.getBlogCard(blogID);
        if(blogCache != null) {
            blogCache.setViews(redisService.getBlogViewsAsCached(blogID));
            return blogCache;
        }

        var blog = mapper.selectBlogByID(blogID);
        if(blog == null) return null;
        var blogTagNames = mapper.selectTagNamesByBlogID(blogID);

        var ret = new BlogCard(blog);
        ret.setContentAbstract(blog.getContentAbstract());
        ret.setTagNames(blogTagNames);
        ret.setViews(redisService.getBlogViewsAsCached(blogID));

        redisService.setBlogCard(blogID, ret);
        return ret;
    }
}
