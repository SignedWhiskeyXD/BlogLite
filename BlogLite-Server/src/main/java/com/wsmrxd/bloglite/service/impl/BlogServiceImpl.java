package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.CacheService;
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
import com.wsmrxd.bloglite.vo.BlogCard;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.BlogPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.wsmrxd.bloglite.enums.RedisKeyForHash.*;
import static com.wsmrxd.bloglite.enums.RedisKeyForHash.SiteInfo_TotalViews;
import static com.wsmrxd.bloglite.enums.RedisKeyForSet.Integer_BlogIDs_ByCollectionID_;
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
    private CacheService cacheService;

    @Autowired
    private MarkDownUtil markDownUtil;

    @Autowired(required = false)
    private BlogSearchServiceRedisImpl rediSearch;

    @Override
    public BlogAdminDetail getBlogAdminDetailByID(int id) {
        var blog = blogMapper.selectBlogByID(id);
        if(blog == null) return null;

        return new BlogAdminDetail(blog,
                blogMapper.selectTagNamesByBlogID(id),
                blogMapper.selectCollectionNamesByBlogID(id));
    }

    @Override
    public int getBlogViewsAsCached(int blogID) {
        Integer ret = cacheService.hash().getValueByHashKey(Integer_BlogViewsByID.name(), Integer.toString(blogID));
        if(ret != null) return ret;

        ret = blogMapper.selectViewsByBlogID(blogID);
        cacheService.hash().putKeyValToHash(Integer_BlogViewsByID.name(), Integer.toString(blogID), ret);
        return ret;
    }

    @Override
    public BlogDetail getBlogDetail(int id) {
        Blog blog = blogMapper.selectBlogByID(id);
        if(blog == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "Blog Not Found");

        var ret = new BlogDetail(blog);
        ret.setContentHTML(markDownUtil.toHtml(blog));
        ret.setTagNames(blogMapper.selectTagNamesByBlogID(id));
        return ret;
    }

    @Override
    @Nullable
    public BlogCard getBlogCard(int blogID){
        var blog = blogMapper.selectBlogByID(blogID);
        if(blog == null) return null;

        var ret = new BlogCard(blog);
        ret.setTagNames(blogMapper.selectTagNamesByBlogID(blogID));

        return ret;
    }

    @Override
    public PageInfo<BlogPreview> getAllBlogsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(blogMapper.selectAllBlogs());
    }

    @Override
    public List<BlogPreview> getBlogRanking(int limit) {
        if(limit < 0) return Collections.emptyList();
        return blogMapper.selectBlogsOrderByViews(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "allBlogTags", allEntries = true)
    public int addNewBlog(BlogUploadInfo newBlog) {
        var newBlogEntity = new Blog(newBlog);
        if(newBlogEntity.getPreviewImage().isEmpty())
            newBlogEntity.setPreviewImage(MarkDownUtil.getPreviewImageUriFromMarkdown(newBlogEntity.getContent()));

        blogMapper.insertBlog(newBlogEntity);

        int newBlogID = newBlogEntity.getId();
        var tagList = newBlog.getTagNames();
        if(tagList != null && !tagList.isEmpty())
            arrangeTagList(newBlogID, tagList);

        var collectionNames = newBlog.getCollections();
        if(collectionNames != null && !collectionNames.isEmpty())
            arrangeCollectionList(newBlogID, collectionNames);

        if(rediSearch != null && !rediSearch.addDocument(newBlogEntity))
            throw new BlogException(ErrorCode.REDISEARCH_ERROR, "Cannot create index for blog, id=" + newBlogID);

        flushSiteInfo();
        addBlogIDtoZSet(newBlogID);
        return newBlogID;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "Blog", key = "#id"),
            @CacheEvict(value = "BlogHTML", key = "#id")
    })
    public void modifyBlog(int id, BlogUploadInfo modifyInfo) {
        blogMapper.updateBlogByModifyInfo(id, new Blog(modifyInfo));
        if(rediSearch != null){
            if(!rediSearch.deleteDocument(id))
                throw new BlogException(ErrorCode.REDISEARCH_ERROR, "Cannot drop index for blog, id=" + id);

            if(!rediSearch.addDocument(id))
                throw new BlogException(ErrorCode.REDISEARCH_ERROR, "Cannot create index for blog, id=" + id);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "TagNamesOfBlog", key = "#blogID"),
            @CacheEvict(value = "allBlogTags", allEntries = true)
    })
    public void reArrangeBlogTag(int blogID, List<String> tagNames){
        blogMapper.deleteTagMappingByBlogID(blogID);
        if(tagNames != null && !tagNames.isEmpty())
            arrangeTagList(blogID, tagNames);
    }

    @Override
    public void reArrangeBlogCollection(int blogID, List<String> collectionNames){
        var blogCollections = blogMapper.selectBlogCollectionByBlogID(blogID);
        for(var blogCollection : blogCollections) {
            cacheService.set().removeSetValue(Integer_BlogIDs_ByCollectionID_.name() + blogCollection.getId(), blogID);
        }

        blogMapper.deleteCollectionMappingByBlogID(blogID);
        if(collectionNames != null && !collectionNames.isEmpty())
            arrangeCollectionList(blogID, collectionNames);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = "Blog", key = "#id"),
            @CacheEvict(value = "TagNamesOfBlog", key = "#id")
    })
    public boolean deleteBlog(int id) {
        flushSiteInfo();
        removeBlogIDFromZSet(id);

        if(rediSearch != null && !rediSearch.deleteDocument(id))
            throw new BlogException(ErrorCode.REDISEARCH_ERROR, "Cannot drop index for blog, id=" + id);

        blogMapper.deleteTagMappingByBlogID(id);
        blogMapper.deleteCollectionMappingByBlogID(id);
        return blogMapper.deleteBlogByID(id);
    }

    @Override
    public void flushSiteInfo() {
        updateBlogViewsFromCache();
        cacheService.delete(Integer_SiteInfo.name());
    }

    @Override
    public Integer getTotalBlogsAsCached() {
        Integer ret = cacheService.hash().getValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalBlogs.name());
        if(ret == null){
            ret = blogMapper.selectBlogCount();
            cacheService.hash().putKeyValToHash(Integer_SiteInfo.name(), SiteInfo_TotalBlogs.name(), ret);
        }
        return ret;
    }

    @Override
    public Integer getTotalViewsAsCached() {
        Integer ret = cacheService.hash().getValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalViews.name());
        if(ret == null){
            ret = blogMapper.selectViewsCount();
            if(ret == null)
                ret = 0;
            cacheService.hash().putKeyValToHash(Integer_SiteInfo.name(), SiteInfo_TotalViews.name(), ret);
        }
        return ret;
    }

    @Override
    public List<Integer> getBlogIDsStartAt(int startID, int blogNum) {
        if(!cacheService.hasKey(Integer_AllBlogIDs.name()))
            cacheAllBlogIDs();

        List<Integer> blogIDs = cacheService.zSet().getListByReversedScoreRange(
                Integer_AllBlogIDs.name(), Double.NEGATIVE_INFINITY, startID, 0, blogNum);

        if(blogIDs != null)
            return blogIDs;
        else return new ArrayList<>();
    }

    @Override
    public List<Integer> getBlogIDsByCollectionIDAsCached(int collectionID){
        List<Integer> cached = cacheService.set()
                .getSetAsList(Integer_BlogIDs_ByCollectionID_.name() + collectionID);

        if(cached != null && !cached.isEmpty())
            return cached;

        var blogIDs = collectionMapper.selectBlogIDsByCollectionID(collectionID);
        for(Integer blogID : blogIDs)
            cacheService.set().addValueToSet(Integer_BlogIDs_ByCollectionID_.name() + collectionID, blogID);

        return blogIDs;
    }

    @Override
    public void increaseBlogViews(int blogID) {
        cacheService.hash().increaseValueByHashKey(Integer_BlogViewsByID.name(), Integer.toString(blogID), 1);
        cacheService.hash().increaseValueByHashKey(Integer_SiteInfo.name(), SiteInfo_TotalViews.name(), 1);
        cacheService.hash().increaseValueByHashKey(Integer_AddBlogViewsByID.name(), Integer.toString(blogID), 1);
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
                cacheService.set().addValueToSet(Integer_BlogIDs_ByCollectionID_.name() + blogCollection.getId(), newBlogID);
            }
        }
    }

    private void cacheAllBlogIDs(){
        cacheService.delete(Integer_AllBlogIDs.name());

        List<Integer> allBlogIDs = blogMapper.selectAllBlogID();
        for(Integer id : allBlogIDs)
            cacheService.zSet().addValueToZSet(Integer_AllBlogIDs.name(), id, id);
    }

    private void updateBlogViewsFromCache(){
        Map<String,Integer> viewsMap = cacheService.hash().getHashEntriesByKey(Integer_AddBlogViewsByID.name());
        if(viewsMap != null){
            var keySet = viewsMap.keySet();
            for (String key : keySet) {
                Integer addNum = viewsMap.get(key);
                blogMapper.updateBlogViewsByID(Integer.parseInt(key), addNum);
            }
        }
        cacheService.delete(Integer_AddBlogViewsByID.name());
    }

    private void addBlogIDtoZSet(int blogID) {
        if(!cacheService.hasKey(Integer_AllBlogIDs.name()))
            cacheAllBlogIDs();

        cacheService.zSet().addValueToZSet(Integer_AllBlogIDs.name(), blogID, blogID);
    }

    private void removeBlogIDFromZSet(int blogID) {
        if(!cacheService.hasKey(Integer_AllBlogIDs.name()))
            cacheAllBlogIDs();

        cacheService.zSet().removeZSetValueByScore(Integer_AllBlogIDs.name(), blogID);
    }
}
