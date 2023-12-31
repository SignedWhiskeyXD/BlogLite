package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;

import java.util.List;

public interface BlogTagService {

    List<BlogTag> getAllTags();

    PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize);

    int addTag(String name);

    boolean removeTag(int id);

    boolean renameTag(int id, String newName);
}
