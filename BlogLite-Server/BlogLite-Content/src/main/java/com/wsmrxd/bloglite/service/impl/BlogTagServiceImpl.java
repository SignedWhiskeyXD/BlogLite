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

import javax.annotation.Nullable;
import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {

    @Autowired
    private BlogTagMapper mapper;

    @Override
    @Cacheable("allBlogTags")
    public List<BlogTag> getAllTags() {
        return mapper.selectAllTags();
    }

    @Override
    public PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mapper.selectAllTags());
    }

    @Override
    @CacheEvict(value = "allBlogTags", allEntries = true)
    public int addTag(@Nullable String name) {
        if(name == null) return -1;

        var newTag = new BlogTag();
        newTag.setTagName(name);
        mapper.insertTag(newTag);
        return newTag.getId();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allBlogTags", allEntries = true),
            @CacheEvict(value = "TagNamesOfBlog", allEntries = true),
    })
    public boolean removeTag(int id) {
        boolean result = mapper.deleteTagByID(id);
        mapper.deleteTagMappingByTagID(id);
        return result;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allBlogTags", allEntries = true),
            @CacheEvict(value = "TagNamesOfBlog", allEntries = true),
    })
    public boolean renameTag(int id, String newName) {
        return mapper.updateTagNameByID(id, newName);
    }
}
