package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {
    private BlogTagMapper mapper;

    @Autowired
    public void setMapper(BlogTagMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Cacheable(value = "BlogTag", key = "#id")
    public BlogTag getTagByID(int id) {
        return mapper.selectTagByID(id);
    }

    @Override
    @Cacheable("allBlogTags")
    public List<BlogTag> getAllTags() {
        return mapper.selectAllTags();
    }

    @Override
    @Cacheable(value = "BlogTagPageInfo", key = "#pageNum + '_' + #pageSize")
    public PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BlogTag> userList = mapper.selectAllTags();
        return new PageInfo<>(userList);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "BlogTagPageInfo", allEntries = true),
            @CacheEvict("allBlogTags")
    })
    public int addTag(String name) {
        var newTag = new BlogTag();
        newTag.setTagName(name);
        mapper.insertTag(newTag);
        return newTag.getId();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "BlogTag", key = "#id"),
            @CacheEvict(value = "BlogTagPageInfo", allEntries = true),
            @CacheEvict("allBlogTags")
    })
    public boolean removeTag(int id) {
        boolean result = mapper.deleteTagByID(id);
        mapper.deleteTagMappingByTagID(id);
        return result;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "BlogTag", key = "#id"),
            @CacheEvict(value = "BlogTagPageInfo", allEntries = true),
            @CacheEvict("allBlogTags")
    })
    public boolean renameTag(int id, String newName) {
        return mapper.updateTagNameByID(id, newName);
    }
}
