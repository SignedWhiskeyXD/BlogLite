package com.wsmrxd.bloglite.service.base;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;

public interface BlogServiceBase {
    Blog getBlogByID(int id);

    PageInfo<Blog> getAllBlogsByPage(int pageNum, int pageSize);

    int addNewBlog(BlogUploadInfo newBlog);

    boolean renameBlogTitle(int id, String newTitle);

    boolean editBlogContent(int id, String newContent);

    boolean addBlogLikes(int id, int moreLikes);

    boolean deleteBlog(int id);
}
