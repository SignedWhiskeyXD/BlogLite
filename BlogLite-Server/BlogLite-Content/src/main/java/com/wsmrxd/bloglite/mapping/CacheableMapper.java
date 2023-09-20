package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class CacheableMapper {

    /*
    * 有一个问题，如果我希望以装饰模式构建其它的VO对象，那么最好缓存装饰所需要的材料，也就是各种实体类
    * 但是mapper接口不能用@Cacheable注解修饰，这些方法直接写在Service类里又有自调用导致的缓存失效问题
    * 干脆把这些方法单独移出来，可以认为这个类只是为mapper方法启用了缓存自动配置能力
    * */

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Cacheable(value = "Blog", key = "#blogID", unless="#result == null")
    @Nullable
    public Blog getBlogEntityByID(int blogID){
        return blogMapper.selectBlogByID(blogID);
    }

    @Cacheable(value = "Comment", key = "#commentID", unless="#result == null")
    @Nullable
    public Comment getCommentByID(int commentID) {
        return commentMapper.selectCommentByID(commentID);
    }

    @Cacheable(value = "TagNamesOfBlog", key = "#blogID", unless="#result == null")
    public List<String> getTagNamesByBlogID(int blogID){
        return blogMapper.selectTagNamesByBlogID(blogID);
    }
}
