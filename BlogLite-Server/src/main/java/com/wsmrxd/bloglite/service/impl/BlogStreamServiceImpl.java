package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.cache.BlogCollectionCache;
import com.wsmrxd.bloglite.cache.CacheService;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wsmrxd.bloglite.enums.RedisKeyForZSet.BlogCard_ByID;

@Service
public class BlogStreamServiceImpl implements BlogStreamService {

    @Autowired
    private BlogMapper mapper;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogCollectionCache blogCollectionCache;

    @Autowired
    private CacheService cacheService;

    private String defaultBlogCardImage;

    @Value("${myConfig.image.defaultBlogCardImage}")
    public void setDefaultBlogCardImage(String defaultBlogCardImage) {
        this.defaultBlogCardImage = defaultBlogCardImage;
    }

    @Override
    public BlogStream getInitStream(int num) {
        List<Integer> latestBlogIDList = blogService.getBlogIDsStartAt(Integer.MAX_VALUE, num);
        return constructBlogStream(latestBlogIDList);
    }

    @Override
    public BlogStream getBlogStream(int startID, int num) {
        List<Integer> latestBlogIDList = blogService.getBlogIDsStartAt(startID, num);
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
        BlogCard blogCache = cacheService.getZSetValueByScore(BlogCard_ByID.name(), blogID);
        if(blogCache != null) {
            blogCache.setViews(blogService.getBlogViewsAsCached(blogID));
            return blogCache;
        }

        var blog = mapper.selectBlogByID(blogID);
        if(blog == null) return null;
        var blogTagNames = mapper.selectTagNamesByBlogID(blogID);

        var ret = new BlogCard(blog);
        ret.setContentAbstract(blog.getContentAbstract());
        ret.setTagNames(blogTagNames);
        ret.setViews(blogService.getBlogViewsAsCached(blogID));
        ret.setPreviewImage(Objects.requireNonNullElse(blog.getPreviewImage(), defaultBlogCardImage));
        if(ret.getPreviewImage().isEmpty())
            ret.setPreviewImage(defaultBlogCardImage);

        cacheService.addValueToZSet(BlogCard_ByID.name(), ret, blogID);
        return ret;
    }
}
