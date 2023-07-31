package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.service.base.BlogServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService implements BlogServiceBase {

    private BlogMapper mapper;

    @Autowired
    public void setMapper(BlogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Blog getBlogByID(int id) {
        return mapper.selectTagByID(id);
    }

    @Override
    public PageInfo<Blog> getAllBlogsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Blog> blogList = mapper.selectAllBlogs();

        return new PageInfo<>(blogList);
    }

    @Override
    public int addNewBlog(BlogUploadInfo newBlog) {
        var newBlogEntity = new Blog();
        newBlogEntity.setTitle(newBlog.getTitle());
        newBlogEntity.setContent(newBlog.getContent());
        return mapper.insertBlog(newBlogEntity);
    }

    @Override
    public boolean renameBlogTitle(int id, String newTitle) {
        return mapper.updateBlogTitleByID(id, newTitle);
    }

    @Override
    public boolean editBlogContent(int id, String newContent) {
        return mapper.updateBlogContentByID(id, newContent);
    }

    @Override
    public boolean addBlogLikes(int id, int moreLikes){
        return mapper.updateBlogLikesByID(id, moreLikes);
    }

    @Override
    public boolean deleteBlog(int id) {
        return mapper.deleteBlogByID(id);
    }
}
