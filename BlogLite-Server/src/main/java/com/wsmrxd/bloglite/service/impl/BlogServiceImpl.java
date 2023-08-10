package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.entity.BlogTagMapping;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPageView;
import com.wsmrxd.bloglite.vo.BlogAdminView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogMapper blogMapper;

    private BlogTagMapper tagMapper;

    private RedisTemplate<String, Object> redisTemplate;

    private MarkDownUtil markDownUtil;

    private final String redisPageKey = "BlogPageInfo";

    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Autowired
    public void setTagMapper(BlogTagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setMarkDownUtil(MarkDownUtil markDownUtil) {
        this.markDownUtil = markDownUtil;
    }

    @Override
    public Blog getBlogByID(int id) {
        return blogMapper.selectBlogByID(id);
    }

    @Override
    public BlogAdminView getBlogViewByID(int id) {
        var redisValOps = redisTemplate.opsForValue();

        BlogAdminView blogAdminViewCache = (BlogAdminView) redisValOps.get("BlogView_" + id);
        if(blogAdminViewCache != null) return blogAdminViewCache;

        var blog = blogMapper.selectBlogByID(id);
        if(blog == null) return null;

        var blogTags = blogMapper.selectTagsByBlogID(id);
        var blogView = new BlogAdminView(blog, blogTags);

        redisValOps.set("BlogView_" + id, blogView);

        return blogView;
    }

    @Override
    public BlogDetail getBlogDetail(int id) {
        var redisValOps = redisTemplate.opsForValue();
        final String redisKey = "BlogDetail_" + id;

        BlogDetail blogDetailCache = (BlogDetail) redisValOps.get(redisKey);
        if(blogDetailCache != null) return blogDetailCache;

        Blog blog = blogMapper.selectBlogByID(id);
        List<String> tagNames = blogMapper.selectTagNamesByBlogID(id);
        BlogDetail blogDetail = new BlogDetail(blog);
        blogDetail.setContentHTML(markDownUtil.toHtml(blog.getContent()));
        blogDetail.setTagNames(tagNames);

        redisValOps.set(redisKey, blogDetail);
        return blogDetail;
    }

    @Override
    public PageInfo<BlogPageView> getAllBlogsByPage(int pageNum, int pageSize) {
        var redisHashOps = redisTemplate.opsForHash();
        final String hashKey = pageNum + "_" + pageSize;

        PageInfo<BlogPageView> blogPageInfoCache = (PageInfo<BlogPageView>) redisHashOps.get(redisPageKey, hashKey);
        if(blogPageInfoCache != null) return blogPageInfoCache;

        PageHelper.startPage(pageNum, pageSize);

        var blogListView = blogMapper.selectAllBlogs();

        var ret = new PageInfo<>(blogListView);
        redisHashOps.put(redisPageKey, hashKey, ret);
        return ret;
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
    public boolean editBlogAbstract(int id, String newAbstract) {
        return blogMapper.updateBlogAbstractByID(id, newAbstract);
    }

    @Override
    public boolean editBlogContent(int id, String newContent) {
        return blogMapper.updateBlogContentByID(id, newContent);
    }

    @Override
    public boolean addBlogViews(int id, int moreViews){
        return blogMapper.updateBlogViewsByID(id, moreViews);
    }

    @Override
    public boolean deleteBlog(int id) {
        return blogMapper.deleteBlogByID(id);
    }

    @Override
    public void flushBlogCache(int blogID){
        redisTemplate.delete("BlogView_" + blogID);
        redisTemplate.delete("BlogDetail_" + blogID);
        redisTemplate.delete("BlogStream_" + blogID);
    }

    @Override
    public void flushBlogPagingCache(){
        redisTemplate.delete(redisPageKey);
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
