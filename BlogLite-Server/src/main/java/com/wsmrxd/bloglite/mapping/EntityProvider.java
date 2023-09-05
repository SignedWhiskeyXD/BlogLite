package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.mapping.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityProvider {

    /*
    * 有一个问题，如果我希望以装饰模式构建其它的VO对象，那么最好缓存装饰所需要的材料，也就是各种实体类
    * 但是mapper接口不能用@Cacheable注解修饰，这些方法直接写在Service类里又有自调用导致的缓存失效问题
    * 干脆把这些方法单独移出来，可以认为这个类只是为mapper方法启用了缓存自动配置能力
    * */

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
