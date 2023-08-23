package com.wsmrxd.bloglite.entity.provider;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityProvider {

    @Autowired
    private BlogMapper blogMapper;

    @Cacheable(value = "Blog", key = "#blogID")
    public Blog getBlogEntityByID(int blogID){
        return blogMapper.selectBlogByID(blogID);
    }

    @Cacheable(value = "TagNamesOfBlog", key = "#blogID")
    public List<String> getTagNamesByBlogID(int blogID){
        return blogMapper.selectTagNamesByBlogID(blogID);
    }

    @Cacheable(value = "CollectionNamesOfBlog", key = "#blogID")
    public List<String> getCollectionNamesByBlogID(int blogID){
        return blogMapper.selectCollectionNamesByBlogID(blogID);
    }
}
