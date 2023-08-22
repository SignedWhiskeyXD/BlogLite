package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.cache.BlogCollectionCache;
import com.wsmrxd.bloglite.entity.BlogCollection;
import com.wsmrxd.bloglite.mapping.BlogCollectionMapper;
import com.wsmrxd.bloglite.service.BlogCollectionService;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogCollectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogCollectionServiceImpl implements BlogCollectionService {

    private BlogCollectionMapper mapper;

    private RedisService redisService;

    private BlogCollectionCache cacheService;

    @Autowired
    public void setMapper(BlogCollectionMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setCacheService(BlogCollectionCache cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public List<BlogCollectionVO> getAllBlogCollectionWithStatistic() {
        var collections = getAllBlogCollection();
        List<BlogCollectionVO> ret = new ArrayList<>(collections.size());
        for(BlogCollection collection : collections){
            BlogCollectionVO collectionVO = new BlogCollectionVO(collection);
            List<Integer> blogIDs = cacheService.getBlogIDsByCollectionIDAsCached(collection.getId());

            collectionVO.setBlogNum(blogIDs.size());
            collectionVO.setTotalViews(getCollectionTotalViewsByBlogIDs(blogIDs));
            ret.add(collectionVO);
        }
        return ret;
    }

    @Cacheable("AllBlogCollection")
    public List<BlogCollection> getAllBlogCollection(){
        return mapper.selectAllBlogCollection();
    }

    private int getCollectionTotalViewsByBlogIDs(List<Integer> blogIDs){
        int ret = 0;
        for(int blogID : blogIDs)
            ret += redisService.getBlogViewsAsCached(blogID);

        return ret;
    }
}
