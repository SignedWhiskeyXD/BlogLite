package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.BlogStreamService;
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogStreamServiceImpl implements BlogStreamService {

    @Autowired
    private BlogService blogService;

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
        List<Integer> blogIDs = blogService.getBlogIDsByCollectionIDAsCached(collectionID);
        return getBlogCardList(blogIDs);
    }

    private BlogStream constructBlogStream(List<Integer> latestBlogIDList) {
        var blogCards = getBlogCardList(latestBlogIDList);

        var initBlogStream = new BlogStream();
        initBlogStream.setBlogNum(blogCards.size());
        initBlogStream.setBlogList(blogCards);
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

    private List<BlogCard> getBlogCardList(List<Integer> blogIDs) {
        List<BlogCard> ret = new ArrayList<>();
        for(int blogID : blogIDs){
            var blogCard = blogService.getBlogCard(blogID);
            if(blogCard != null) {
                setViewsAndImageToBlogCard(blogCard);
                ret.add(blogCard);
            }
        }
        return ret;
    }

    private void setViewsAndImageToBlogCard(BlogCard blogCard){
        blogCard.setViews(blogService.getBlogViewsAsCached(blogCard.getId()));
        if(blogCard.getPreviewImage().isEmpty())
            blogCard.setPreviewImage(defaultBlogCardImage);
    }
}
