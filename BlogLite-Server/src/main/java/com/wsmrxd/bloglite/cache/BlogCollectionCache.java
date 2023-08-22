package com.wsmrxd.bloglite.cache;

import com.wsmrxd.bloglite.mapping.BlogCollectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BlogCollectionCache {

    private RedisTemplate<String, Object> redisTemplate;

    private BlogCollectionMapper mapper;

    private final static String setKeyPrefix = "BlogIDSet_BlogCollection_";

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setMapper(BlogCollectionMapper mapper) {
        this.mapper = mapper;
    }

    public List<Integer> getBlogIDsByCollectionIDAsCached(int collectionID){
        var redisSetOps = redisTemplate.opsForSet();
        var cached = redisSetOps.members(setKeyPrefix + collectionID);

        if(cached != null && cached.size() > 0)
            return convertSetToIntegerList(cached);

        var blogIDs = mapper.selectBlogIDsByCollectionID(collectionID);
        for(Integer blogID : blogIDs)
            redisSetOps.add(setKeyPrefix + collectionID, blogID);

        return blogIDs;
    }

    public void addBlogIDToSet(int collectionID, int blogID){
        var redisSetOps = redisTemplate.opsForSet();
        redisSetOps.add(setKeyPrefix + collectionID, blogID);
    }

    public void removeBlogIDFromSet(int collectionID, int blogID){
        var redisSetOps = redisTemplate.opsForSet();
        redisSetOps.remove(setKeyPrefix + collectionID, blogID);
    }

    private List<Integer> convertSetToIntegerList(Set<Object> set){
        var ret = new ArrayList<Integer>(set.size());

        for(Object obj : set)
            ret.add((Integer) obj);
        return ret;
    }
}
