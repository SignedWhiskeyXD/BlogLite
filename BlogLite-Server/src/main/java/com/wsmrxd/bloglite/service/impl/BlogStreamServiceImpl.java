package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.cache.BlogCollectionCache;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BlogStreamServiceImpl implements BlogStreamService {

    private BlogMapper mapper;

    private RedisService redisService;

    private BlogCollectionCache blogCollectionCache;

    private String defaultBlogCardImage;

    @Value("${myConfig.image.defaultBlogCardImage}")
    public void setDefaultBlogCardImage(String defaultBlogCardImage) {
        this.defaultBlogCardImage = defaultBlogCardImage;
    }

    @Autowired
    public void setMapper(BlogMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setBlogCollectionCache(BlogCollectionCache blogCollectionCache) {
        this.blogCollectionCache = blogCollectionCache;
    }

    @Override
    public BlogStream getInitStream(int num) {
        List<Integer> latestBlogIDList = redisService.getBlogIDsStartAt(Integer.MAX_VALUE, num);
        return constructBlogStream(latestBlogIDList);
    }

    @Override
    public BlogStream getBlogStream(int startID, int num) {
        List<Integer> latestBlogIDList = redisService.getBlogIDsStartAt(startID, num);
        return constructBlogStream(latestBlogIDList);
    }

    @Override
    public List<BlogCard> getAllBlogsFromCollection(int collectionID) {
        List<Integer> blogIDs = blogCollectionCache.getBlogIDsByCollectionIDAsCached(collectionID);
        List<BlogCard> ret = new ArrayList<>();
        for(Integer blogID : blogIDs){
            ret.add(getBlogCard(blogID));
        }
        return ret;
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
        if(!latestBlogIDList.isEmpty()) {
            int nextRequestParam = latestBlogIDList.get(latestBlogIDList.size() - 1) - 1;
            if(nextRequestParam < 1)
                initBlogStream.setNextRequestParam(null);
            else
                initBlogStream.setNextRequestParam(nextRequestParam);
        }
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
        ret.setPreviewImage(Objects.requireNonNullElse(blog.getPreviewImage(), defaultBlogCardImage));
        if(ret.getPreviewImage().isEmpty())
            ret.setPreviewImage(defaultBlogCardImage);

        redisService.setBlogCard(blogID, ret);
        return ret;
    }
}
