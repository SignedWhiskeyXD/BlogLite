package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import com.wsmrxd.bloglite.cache.BlogCollectionCache;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogCollectionMapping;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.entity.BlogTagMapping;
import com.wsmrxd.bloglite.mapping.BlogCollectionMapper;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.RedisService;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BlogServiceImpl implements BlogService {

    private BlogMapper blogMapper;

    private BlogTagMapper tagMapper;

    private BlogCollectionMapper collectionMapper;

    private BlogCollectionCache blogCollectionCache;

    private RedisService redisService;

    private MarkDownUtil markDownUtil;

    @Autowired
    public void setBlogMapper(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Autowired
    public void setTagMapper(BlogTagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Autowired
    public void setCollectionMapper(BlogCollectionMapper collectionMapper) {
        this.collectionMapper = collectionMapper;
    }

    @Autowired
    public void setBlogCollectionCache(BlogCollectionCache blogCollectionCache) {
        this.blogCollectionCache = blogCollectionCache;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
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
    @Cacheable(value = "BlogAdminDetail", key = "#id")
    public BlogAdminDetail getBlogAdminDetailByID(int id) {
        var blog = blogMapper.selectBlogByID(id);
        if(blog == null) return null;

        var blogTagNames = blogMapper.selectTagNamesByBlogID(id);
        var blogCollectionNames = blogMapper.selectCollectionNamesByBlogID(id);
        return new BlogAdminDetail(blog, blogTagNames, blogCollectionNames);
    }

    @Override
    public BlogDetail getBlogDetail(int id) {
        BlogDetail ret = redisService.getBlogDetail(id);
        if(ret != null) {
            ret.setViews(redisService.getBlogViewsAsCached(id) + 1);
            redisService.increaseBlogViews(id);
            return ret;
        }

        Blog blog = blogMapper.selectBlogByID(id);
        List<String> tagNames = blogMapper.selectTagNamesByBlogID(id);
        ret = new BlogDetail(blog);
        ret.setContentHTML(markDownUtil.toHtml(blog.getContent()));
        ret.setTagNames(tagNames);
        ret.setViews(redisService.getBlogViewsAsCached(id) + 1);

        redisService.setBlogDetail(id, ret);
        redisService.increaseBlogViews(id);
        return ret;
    }

    @Override
    public PageInfo<BlogPreview> getAllBlogsByPage(int pageNum, int pageSize) {
        PageInfo<BlogPreview> blogPageInfoCache = redisService.getBlogPreviewPage(pageNum, pageSize);
        if(blogPageInfoCache != null) return blogPageInfoCache;

        PageHelper.startPage(pageNum, pageSize);
        var ret = new PageInfo<>(blogMapper.selectAllBlogs());

        redisService.setBlogPreviewPage(pageNum, pageSize, ret);
        return ret;
    }

    @Override
    public List<BlogTag> getAllTagsByBlogID(int blogID) {
        return blogMapper.selectTagsByBlogID(blogID);
    }

    @Override
    @CacheEvict("allBlogTags")
    public int addNewBlog(BlogUploadInfo newBlog) {
        var newBlogEntity = new Blog();
        newBlogEntity.setTitle(newBlog.getTitle());
        newBlogEntity.setContent(newBlog.getContent());
        newBlogEntity.setPreviewImage(Objects.requireNonNullElse(newBlog.getPreviewImage(), ""));
        blogMapper.insertBlog(newBlogEntity);

        int newBlogID = newBlogEntity.getId();
        var tagList = newBlog.getTagNames();
        if(tagList != null && tagList.size() > 0)
            arrangeTagList(newBlogID, tagList);

        var collectionNames = newBlog.getCollections();
        if(collectionNames != null && collectionNames.size() > 0)
            arrangeCollectionList(newBlogID, collectionNames);

        redisService.flushSiteInfo();
        redisService.addBlogIDtoZSet(newBlogID);
        return newBlogID;
    }
    @Override

    @Caching(evict = {
            @CacheEvict(value = "BlogAdminDetail", key = "#blogID"),
            @CacheEvict("allBlogTags")
    })
    public void reArrangeBlogTag(int blogID, List<String> tagNames){
        blogMapper.deleteTagMappingByBlogID(blogID);
        if(tagNames != null && tagNames.size() > 0)
            arrangeTagList(blogID, tagNames);
    }

    @Override
    @CacheEvict(value = "BlogAdminDetail", key = "#blogID")
    public void reArrangeBlogCollection(int blogID, List<String> collectionNames){
        var blogCollections = blogMapper.selectBlogCollectionByBlogID(blogID);
        for(var blogCollection : blogCollections)
            blogCollectionCache.removeBlogIDFromSet(blogCollection.getId(), blogID);

        blogMapper.deleteCollectionMappingByBlogID(blogID);
        if(collectionNames != null && collectionNames.size() > 0)
            arrangeCollectionList(blogID, collectionNames);
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
    public boolean deleteBlog(int id) {
        redisService.flushSiteInfo();
        redisService.removeBlogIDFromZSet(id);
        return blogMapper.deleteBlogByID(id);
    }

    @Override
    public void flushBlogCache(int blogID){
        redisService.flushBlogCache(blogID);
    }

    @Override
    public void flushBlogPagingCache(){
        redisService.flushBlogPagingCache();
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

    private void arrangeCollectionList(int newBlogID, List<String> collectionNames){
        for(String collectionName: collectionNames){
            var blogCollection = collectionMapper.selectBlogCollectionByName(collectionName);
            if(blogCollection != null) {
                blogMapper.insertBlogCollectionMapping(new BlogCollectionMapping(newBlogID, blogCollection.getId()));
                blogCollectionCache.addBlogIDToSet(blogCollection.getId(), newBlogID);
            }
        }
    }
}
