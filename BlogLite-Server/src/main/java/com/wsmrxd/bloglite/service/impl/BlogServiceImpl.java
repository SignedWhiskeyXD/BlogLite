package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import com.wsmrxd.bloglite.cache.BlogCollectionCache;
import com.wsmrxd.bloglite.cache.CacheService;
import com.wsmrxd.bloglite.dto.BlogUploadInfo;
import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.BlogCollectionMapping;
import com.wsmrxd.bloglite.entity.BlogTag;
import com.wsmrxd.bloglite.entity.BlogTagMapping;
import com.wsmrxd.bloglite.mapping.BlogCollectionMapper;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import com.wsmrxd.bloglite.mapping.BlogTagMapper;
import com.wsmrxd.bloglite.service.BlogService;
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
import java.util.Map;

import static com.wsmrxd.bloglite.enums.RedisKeyForHash.*;
import static com.wsmrxd.bloglite.enums.RedisKeyForHash.SiteInfo_TotalViews;
import static com.wsmrxd.bloglite.enums.RedisKeyForZSet.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogCollectionMapper collectionMapper;

    @Autowired
    private BlogCollectionCache blogCollectionCache;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MarkDownUtil markDownUtil;

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
    public int getBlogViewsAsCached(int blogID) {
        Integer ret = cacheService.getValueByHashKey(Integer_BlogViewsByID.name(), Integer.toString(blogID));
        if(ret != null) return ret;

        ret = blogMapper.selectViewsByBlogID(blogID);
        cacheService.putKeyValToHash(Integer_BlogViewsByID.name(), Integer.toString(blogID), ret);
        return ret;
    }

    // TODO: 使用缓存自动配置
    @Override
    public BlogDetail getBlogDetail(int id) {
        BlogDetail ret = cacheService.getZSetValueByScore(BlogDetail_ByID.name(), id);
        if(ret != null) {
            ret.setViews(getBlogViewsAsCached(id) + 1);
            increaseBlogViews(id);
            return ret;
        }

        Blog blog = blogMapper.selectBlogByID(id);
        List<String> tagNames = blogMapper.selectTagNamesByBlogID(id);
        ret = new BlogDetail(blog);
        ret.setContentHTML(markDownUtil.toHtml(blog.getContent()));
        ret.setTagNames(tagNames);
        ret.setViews(getBlogViewsAsCached(id) + 1);

        cacheService.addValueToZSet(BlogDetail_ByID.name(), ret, id);
        increaseBlogViews(id);
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

        flushSiteInfo();
        addBlogIDtoZSet(newBlogID);
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

        cacheService.removeZSetValueByScore(BlogDetail_ByID.name(), id);
        cacheService.removeZSetValueByScore(BlogCard_ByID.name(), id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "BlogAdminDetail", key = "#id"),
            @CacheEvict(value = "BlogPaging", allEntries = true)
    })
    public boolean deleteBlog(int id) {
        flushSiteInfo();
        removeBlogIDFromZSet(id);
        cacheService.removeZSetValueByScore(BlogDetail_ByID.name(), id);
        cacheService.removeZSetValueByScore(BlogCard_ByID.name(), id);
        blogMapper.deleteTagMappingByBlogID(id);
        blogMapper.deleteCollectionMappingByBlogID(id);
        return blogMapper.deleteBlogByID(id);
    }

    @Override
    public void flushSiteInfo() {
        updateBlogViewsFromCache();
        cacheService.delete(Integer_SiteInfo.name());
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

    private void cacheAllBlogIDs(){
        cacheService.delete(Integer_AllBlogIDs.name());

        List<Integer> allBlogIDs = blogMapper.selectAllBlogID();
        for(Integer id : allBlogIDs)
            cacheService.addValueToZSet(Integer_AllBlogIDs.name(), id, id);
    }

    private void updateBlogViewsFromCache(){
        Map<Integer, Integer> viewsMap = cacheService.getHashEntriesByKey(Integer_AddBlogViewsByID.name());
        var keySet = viewsMap.keySet();
        for(Integer key : keySet){
            Integer addNum = viewsMap.get(key);
            blogMapper.updateBlogViewsByID(key, addNum);
        }
        cacheService.delete(Integer_AddBlogViewsByID.name());
    }

    private void increaseBlogViews(int blogID) {
        cacheService.increaseValueByHashKey(Integer_BlogViewsByID.name(), Integer.toString(blogID), 1);
        cacheService.increaseValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalViews.name(), 1);
        cacheService.increaseValueByHashKey(Integer_AddBlogViewsByID.name(), Integer.toString(blogID), 1);
    }

    private void addBlogIDtoZSet(int blogID) {
        if(!cacheService.hasKey(Integer_AllBlogIDs.name()))
            cacheAllBlogIDs();

        cacheService.addValueToZSet(Integer_AllBlogIDs.name(), blogID, blogID);
    }

    private void removeBlogIDFromZSet(int blogID) {
        if(!cacheService.hasKey(Integer_AllBlogIDs.name()))
            cacheAllBlogIDs();

        cacheService.removeZSetValueByScore(Integer_AllBlogIDs.name(), blogID);
    }
}
