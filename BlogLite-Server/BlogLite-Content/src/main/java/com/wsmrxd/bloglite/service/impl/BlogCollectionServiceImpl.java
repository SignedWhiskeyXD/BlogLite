package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.dto.BlogCollectionCreateInfo;
import com.wsmrxd.bloglite.entity.BlogCollection;
import com.wsmrxd.bloglite.mapping.BlogCollectionMapper;
import com.wsmrxd.bloglite.mapping.CacheableMapper;
import com.wsmrxd.bloglite.service.BlogCollectionService;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.vo.BlogCollectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.wsmrxd.bloglite.enums.RedisKeyForSet.Integer_BlogIDs_ByCollectionID_;

@Service
public class BlogCollectionServiceImpl implements BlogCollectionService {

    @Autowired
    private BlogCollectionMapper collectionMapper;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheableMapper cacheableMapper;

    @Value("${myConfig.image.defaultCollectionImage}")
    private String defaultCollectionImageUrl;

    @Override
    public List<BlogCollectionVO> getBlogCollectionVOList(List<BlogCollection> collections) {
        List<BlogCollectionVO> ret = new ArrayList<>(collections.size());
        for(BlogCollection collection : collections){
            BlogCollectionVO collectionVO = new BlogCollectionVO(collection);
            List<Integer> blogIDs = blogService.getBlogIDsByCollectionIDAsCached(collection.getId());

            collectionVO.setBlogNum(blogIDs.size());
            collectionVO.setTotalViews(getCollectionTotalViewsByBlogIDs(blogIDs));
            ret.add(collectionVO);
        }
        return ret;
    }

    @Override
    @Cacheable("AllBlogCollection")
    public List<BlogCollection> getAllBlogCollection(){
        return collectionMapper.selectAllBlogCollection();
    }

    @Override
    @CacheEvict("AllBlogCollection")
    public void modifyCollectionInfo(BlogCollection modifyInfo) {
        String imageUrl = modifyInfo.getImageLink();
        if(imageUrl == null || imageUrl.isEmpty())
            modifyInfo.setImageLink(defaultCollectionImageUrl);

        collectionMapper.updateBlogCollection(modifyInfo);
    }

    @Override
    @CacheEvict("AllBlogCollection")
    public void createNewCollection(BlogCollectionCreateInfo newCollection) {
        var newCollectionEntity = new BlogCollection();
        newCollectionEntity.setCollectionName(newCollection.getCollectionName());
        newCollectionEntity.setDescription(newCollection.getDescription());
        String imageLink = newCollection.getImageLink();
        if(imageLink != null && imageLink.length() > 0)
            newCollectionEntity.setImageLink(imageLink);
        else
            newCollectionEntity.setImageLink(defaultCollectionImageUrl);

        collectionMapper.insertBlogCollection(newCollectionEntity);
    }

    @Override
    @CacheEvict("AllBlogCollection")
    public void removeBlogCollection(int collectionID) {
        collectionMapper.deleteBlogCollectionMapping(collectionID);
        collectionMapper.deleteBlogCollectionByID(collectionID);
        cacheService.delete(Integer_BlogIDs_ByCollectionID_.name() + collectionID);
    }

    private int getCollectionTotalViewsByBlogIDs(List<Integer> blogIDs){
        int ret = 0;
        for(int blogID : blogIDs)
            ret += blogService.getBlogViewsAsCached(blogID);

        return ret;
    }
}
