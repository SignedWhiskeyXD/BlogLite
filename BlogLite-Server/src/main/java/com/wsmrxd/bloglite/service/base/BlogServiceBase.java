package com.wsmrxd.bloglite.service.base;

import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.vo.BlogPageView;

import java.util.List;

public interface BlogServiceBase {
    Blog getBlogByID(int id);

    PageInfo<BlogPageView> getAllBlogsByPage(int pageNum, int pageSize);

    List<BlogTag> getAllTagsByBlogID(int blogID);

    int addNewBlog(BlogUploadInfo newBlog);

    void reArrangeBlogTag(int blogID, List<String> tagNames);

    boolean renameBlogTitle(int id, String newTitle);

    boolean editBlogContent(int id, String newContent);

    boolean addBlogLikes(int id, int moreLikes);

    boolean deleteBlog(int id);
}
