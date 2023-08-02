package com.wsmrxd.bloglite.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.entity.BlogTagMapping;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.base.BlogServiceBase;
import com.wsmrxd.bloglite.vo.BlogPageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService implements BlogServiceBase {

    private BlogMapper blogMapper;

    private BlogTagMapper tagMapper;

    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Autowired
    public void setTagMapper(BlogTagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public Blog getBlogByID(int id) {
        return blogMapper.selectBlogByID(id);
    }

    @Override
    public PageInfo<BlogPageView> getAllBlogsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        var blogListView = blogMapper.selectAllBlogs();

        return new PageInfo<>(blogListView);
    }

    @Override
    public List<BlogTag> getAllTagsByBlogID(int blogID) {
        return blogMapper.selectTagsByBlogID(blogID);
    }

    @Override
    public int addNewBlog(BlogUploadInfo newBlog) {
        var newBlogEntity = new Blog();
        newBlogEntity.setTitle(newBlog.getTitle());
        newBlogEntity.setContent(newBlog.getContent());
        blogMapper.insertBlog(newBlogEntity);

        int newBlogID = newBlogEntity.getId();
        var tagList = newBlog.getTagNames();
        if(tagList != null && tagList.size() > 0)
            arrangeTagList(newBlogID, tagList);

        return newBlogID;
    }
    @Override
    public void reArrangeBlogTag(int blogID, List<String> tagNames){
        blogMapper.deleteTagMappingByBlogID(blogID);
        arrangeTagList(blogID, tagNames);
    }

    @Override
    public boolean renameBlogTitle(int id, String newTitle) {
        return blogMapper.updateBlogTitleByID(id, newTitle);
    }

    @Override
    public boolean editBlogContent(int id, String newContent) {
        return blogMapper.updateBlogContentByID(id, newContent);
    }

    @Override
    public boolean addBlogLikes(int id, int moreLikes){
        return blogMapper.updateBlogLikesByID(id, moreLikes);
    }

    @Override
    public boolean deleteBlog(int id) {
        return blogMapper.deleteBlogByID(id);
    }

    private void arrangeTagList(int newBlogID, List<String> tagList) {
        for(String tagName : tagList){
            var tag = tagMapper.selectTagByName(tagName);
            if(tag != null){
                blogMapper.insertBlogTagMapping(new BlogTagMapping(newBlogID, tag.getId()));
            }else{
                var newTag = new BlogTag();
                newTag.setTagName(tagName);
                tagMapper.insertTag(newTag);
                int newTagID = newTag.getId();
                blogMapper.insertBlogTagMapping(new BlogTagMapping(newBlogID, newTagID));
            }
        }
    }
}
