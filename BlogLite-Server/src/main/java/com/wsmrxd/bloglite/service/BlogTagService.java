package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.base.BlogTagServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

import java.util.List;

@Service
public class BlogTagService implements BlogTagServiceBase {
    @Autowired
    public void setMapper(BlogTagMapper mapper) {
        this.mapper = mapper;
    }

    BlogTagMapper mapper;

    @Override
    public BlogTag getTagByID(int id) {
        return mapper.selectTagByID(id);
    }

    @Override
    public BlogTag getTagByName(String name) {
        return mapper.selectTagByName(name);
    }

    @Override
    public PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<BlogTag> userList = mapper.selectAllTags();

        return new PageInfo<>(userList);
    }

    @Override
    public boolean addTag(String name) {
        return mapper.insertTagFromName(name);
    }

    @Override
    public boolean removeTag(int id) {
        return mapper.deleteTagByID(id);
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
}
