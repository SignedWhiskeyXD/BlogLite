package com.wsmrxd.bloglite.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmrxd.bloglite.Utils.MarkDownUtil;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
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
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogCollectionMapper collectionMapper;

    @Autowired
    private MarkDownUtil markDownUtil;

    @Autowired(required = false)
    private BlogSearchServiceRedisImpl rediSearch;

    /* 倒序排列的文章ID集合 */
    private final TreeSet<Integer> blogIDs = new TreeSet<>((lhs, rhs) -> Integer.compare(rhs, lhs));

    @PostConstruct
    public void fillBlogIDs() {
        blogIDs.addAll(blogMapper.selectAllBlogID());
    }

    @Override
    public BlogAdminDetail getBlogAdminDetailByID(int id) {
        var blog = blogMapper.selectBlogByID(id);
        if(blog == null) return null;

        return new BlogAdminDetail(blog,
                blogMapper.selectTagNamesByBlogID(id),
                blogMapper.selectCollectionNamesByBlogID(id));
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

        blogIDs.add(newBlogID);
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
        blogIDs.remove(id);

        if(rediSearch != null && !rediSearch.deleteDocument(id))
            throw new BlogException(ErrorCode.REDISEARCH_ERROR, "Cannot drop index for blog, id=" + id);

        blogMapper.deleteTagMappingByBlogID(id);
        blogMapper.deleteCollectionMappingByBlogID(id);
        return blogMapper.deleteBlogByID(id);
    }

    @Override
    public List<Integer> getBlogIDsStartAt(int startID, int blogNum) {
        /* 由于集合是倒序排列的，实际上这等效于获取正序集合的headSet */
        return blogIDs.tailSet(startID)
                .stream()
                .limit(blogNum)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getBlogIDsByCollectionIDAsCached(int collectionID){
        return collectionMapper.selectBlogIDsByCollectionID(collectionID);
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
        for(String collectionName: collectionNames) {
            var blogCollection = collectionMapper.selectBlogCollectionByName(collectionName);
            if(blogCollection == null) continue;

            blogMapper.insertBlogCollectionMapping(new BlogCollectionMapping(newBlogID, blogCollection.getId()));
        }
    }
}
