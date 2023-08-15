package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.BlogTagService;
import com.wsmrxd.bloglite.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {
    private BlogTagMapper mapper;

    private RedisService redisService;

    @Autowired
    public void setMapper(BlogTagMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public BlogTag getTagByID(int id) {
        BlogTag blogTagCache = redisService.getBlogTag(id);
        if(blogTagCache != null) return blogTagCache;

        BlogTag ret = mapper.selectTagByID(id);
        redisService.setBlogTag(ret);
        return ret;
    }

    @Override
    public List<BlogTag> getAllTags() {
        List<BlogTag> ret = redisService.getAllBlogTags();
        if(ret == null){
            ret = mapper.selectAllTags();
            redisService.setAllBlogTags(ret);
        }
        return ret;
    }

    @Override
    public PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize) {
        PageInfo<BlogTag> tagPageInfoCache =  redisService.getTagPageInfo(pageNum, pageSize);
        if(tagPageInfoCache != null) return tagPageInfoCache;

        PageHelper.startPage(pageNum, pageSize);

        List<BlogTag> userList = mapper.selectAllTags();

        var ret = new PageInfo<>(userList);
        redisService.setTagPageInfo(pageNum, pageSize, ret);
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
    public boolean renameTag(int id, String newName) {
        return mapper.updateTagNameByID(id, newName);
    }

    @Override
    public void flushPageInfoCache(){
        redisService.flushTagPageInfoCache();
    }

    @Override
    public void flushTagCache(int id){
        redisService.flushTagCache(id);
    }
}
