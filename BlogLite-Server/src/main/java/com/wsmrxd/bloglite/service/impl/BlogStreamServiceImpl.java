package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.vo.BlogStream;
import com.wsmrxd.bloglite.vo.BlogStreamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogStreamServiceImpl implements BlogStreamService {

    private BlogMapper mapper;

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setMapper(BlogMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
        var blogItems = new ArrayList<BlogStreamItem>();

        for(int blogID : latestBlogIDList){
            var blogItem = getBlogStreamItem(blogID);
            if(blogItem != null)
                blogItems.add(blogItem);
        }

        var initBlogStream = new BlogStream();
        initBlogStream.setBlogNum(blogItems.size());
        initBlogStream.setBlogList(blogItems);
        if(!latestBlogIDList.isEmpty())
            initBlogStream.setNextRequestParam(latestBlogIDList.get(latestBlogIDList.size() - 1));

        return initBlogStream;
    }

    private BlogStreamItem getBlogStreamItem(int blogID){
        var redisValOps = redisTemplate.opsForValue();
        final String redisKey = "BlogStream_" + blogID;

        BlogStreamItem blogCache = (BlogStreamItem) redisValOps.get(redisKey);
        if(blogCache != null) return blogCache;

        var blog = mapper.selectBlogByID(blogID);
        if(blog == null) return null;
        var blogTagNames = mapper.selectTagNamesByBlogID(blogID);

        var ret = new BlogStreamItem(blog);
        ret.setContentAbstract(blog.getContentAbstract());
        ret.setTagNames(blogTagNames);

        redisValOps.set(redisKey, ret);
        return ret;
    }
}
