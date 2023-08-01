package com.wsmrxd.bloglite.service.base;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.entity.BlogTag;

import java.util.List;

public interface BlogTagServiceBase {
    BlogTag getTagByID(int id);

    BlogTag getTagByName(String name);

    PageInfo<BlogTag> getAllTagsByPage(int pageNum, int pageSize);

    int addTag(String name);

    boolean removeTag(int id);

    boolean removeTag(String name);

    boolean renameTag(int id, String newName);

    boolean renameTag(String oldName, String newName);
}
