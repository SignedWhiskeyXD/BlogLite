package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {
    private BlogTagMapper mapper;

    private RedisTemplate<String, Object> redisTemplate;

    private final String redisPageKey = "TagPageInfo";

    @Autowired
    public void setMapper(BlogTagMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public BlogTag getTagByID(int id) {
        var redisValOps = redisTemplate.opsForValue();

        BlogTag blogTagCache = (BlogTag) redisValOps.get("BlogTag_" + id);
        if(blogTagCache != null) return blogTagCache;

        BlogTag ret = mapper.selectTagByID(id);
        redisValOps.set("BlogTag_" + id, ret);
        return ret;
    }

    @Override
    public BlogTag getTagByName(String name) {
        return mapper.selectTagByName(name);
    }

    @Override
    public List<BlogTag> getAllTags() {
        return mapper.selectAllTags();
    }

    @Override
    public PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize) {
        var redisHashOps = redisTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        PageInfo<BlogTag> tagPageInfoCache = (PageInfo<BlogTag>) redisHashOps.get(redisPageKey, hashKey);
        if(tagPageInfoCache != null) return tagPageInfoCache;

        PageHelper.startPage(pageNum, pageSize);

        List<BlogTag> userList = mapper.selectAllTags();

        var ret = new PageInfo<>(userList);
        redisHashOps.put(redisPageKey, hashKey, ret);
        return ret;
    }

    @Override
    public int addTag(String name) {
        var newTag = new BlogTag();
        newTag.setTagName(name);
        mapper.insertTag(newTag);
        return newTag.getId();
    }

    @Override
    public boolean removeTag(int id) {
        boolean result = mapper.deleteTagByID(id);
        result &= mapper.deleteTagMappingByTagID(id);
        return result;
    }

    @Override
    public boolean removeTag(String name) {
        return mapper.deleteTagByName(name);
    }

    @Override
    public boolean renameTag(int id, String newName) {
        return mapper.updateTagNameByID(id, newName);
    }

    @Override
    public boolean renameTag(String oldName, String newName) {
        return mapper.updateTagNameByName(oldName, newName);
    }

    @Override
    public void flushPageInfoCache(){
        redisTemplate.delete(redisPageKey);
    }

    @Override
    public void flushTagCache(int id){
        redisTemplate.delete("BlogTag_" + id);
    }
}
