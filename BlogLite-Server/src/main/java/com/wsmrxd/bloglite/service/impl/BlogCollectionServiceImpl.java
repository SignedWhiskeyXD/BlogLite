package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.dto.BlogCollectionCreateInfo;
import com.wsmrxd.bloglite.entity.BlogCollection;
import com.wsmrxd.bloglite.mapping.BlogCollectionMapper;
import com.wsmrxd.bloglite.service.BlogCollectionService;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.vo.BlogCollectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogCollectionServiceImpl implements BlogCollectionService {

    @Autowired
    private BlogCollectionMapper mapper;

    @Autowired
    private BlogService blogService;

    private String defaultCollectionImageUrl;

    @Value("${myConfig.image.defaultCollectionImage}")
    public void setDefaultCollectionImageUrl(String defaultCollectionImageUrl) {
        this.defaultCollectionImageUrl = defaultCollectionImageUrl;
    }

    @Override
    public List<BlogCollectionVO> getAllBlogCollectionWithStatistic() {
        var collections = getAllBlogCollection();
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
    public void modifyCollectionInfo(BlogCollection modifyInfo) {
        String imageUrl = modifyInfo.getImageLink();
        if(imageUrl == null || imageUrl.isEmpty())
            modifyInfo.setImageLink(defaultCollectionImageUrl);

        mapper.updateBlogCollection(modifyInfo);
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

        mapper.insertBlogCollection(newCollectionEntity);
    }

    @Override
    @CacheEvict("AllBlogCollection")
    public void removeBlogCollection(int collectionID) {
        mapper.deleteBlogCollectionMapping(collectionID);
        mapper.deleteBlogCollectionByID(collectionID);
    }

    @Cacheable("AllBlogCollection")
    public List<BlogCollection> getAllBlogCollection(){
        return mapper.selectAllBlogCollection();
    }

    private int getCollectionTotalViewsByBlogIDs(List<Integer> blogIDs){
        int ret = 0;
        for(int blogID : blogIDs)
            ret += blogService.getBlogViewsAsCached(blogID);

        return ret;
    }
}
