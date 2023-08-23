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
import com.wsmrxd.bloglite.vo.BlogAdminDetail;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Cacheable(value = "BlogPaging", key = "#pageNum + '_' + #pageSize")
    public PageInfo<BlogPreview> getAllBlogsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(blogMapper.selectAllBlogs());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    @Caching(evict = {
            @CacheEvict(value = "BlogPaging", allEntries = true),
            @CacheEvict(value = "allBlogTags", allEntries = true)
    })
    public int addNewBlog(BlogUploadInfo newBlog) {
        var newBlogEntity = new Blog(newBlog);
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
    @Transactional(rollbackFor = {Exception.class})
    @Caching(evict = {
            @CacheEvict(value = "BlogAdminDetail", key = "#id"),
            @CacheEvict(value = "BlogPaging", allEntries = true),
            @CacheEvict(value = "allBlogTags", allEntries = true)
    })
    public void modifyBlog(int id, BlogUploadInfo modifyInfo) {
        blogMapper.updateBlogByModifyInfo(id, modifyInfo);
        reArrangeBlogTag(id, modifyInfo.getTagNames());
        reArrangeBlogCollection(id, modifyInfo.getCollections());
        redisService.flushBlogCache(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "BlogAdminDetail", key = "#id"),
            @CacheEvict(value = "BlogPaging", allEntries = true)
    })
    public boolean deleteBlog(int id) {
        redisService.flushSiteInfo();
        redisService.removeBlogIDFromZSet(id);
        redisService.flushBlogCache(id);
        blogMapper.deleteTagMappingByBlogID(id);
        blogMapper.deleteCollectionMappingByBlogID(id);
        return blogMapper.deleteBlogByID(id);
    }

    private void reArrangeBlogTag(int blogID, List<String> tagNames){
        blogMapper.deleteTagMappingByBlogID(blogID);
        if(tagNames != null && tagNames.size() > 0)
            arrangeTagList(blogID, tagNames);
    }

    private void reArrangeBlogCollection(int blogID, List<String> collectionNames){
        var blogCollections = blogMapper.selectBlogCollectionByBlogID(blogID);
        for(var blogCollection : blogCollections)
            blogCollectionCache.removeBlogIDFromSet(blogCollection.getId(), blogID);

        blogMapper.deleteCollectionMappingByBlogID(blogID);
        if(collectionNames != null && collectionNames.size() > 0)
            arrangeCollectionList(blogID, collectionNames);
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
