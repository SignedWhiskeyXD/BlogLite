package com.wsmrxd.bloglite.aspect;

import com.wsmrxd.bloglite.entity.Blog;
import com.wsmrxd.bloglite.entity.Comment;
import com.wsmrxd.bloglite.service.CacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;

@Aspect
@Component
public class DBCacheAspect {

    @Autowired
    private CacheService cacheService;

    @Around("execution(* com.wsmrxd.bloglite.mapping.BlogMapper.selectBlogByID(int)) && args(blogID)")
    public Object cacheBlogEntity(ProceedingJoinPoint pjp, int blogID) throws Throwable {
        String redisKey = "Blog::" + blogID;
        Blog blogCached = cacheService.keyVal().getValueByKey(redisKey);
        if(blogCached != null) return blogCached;

        Object ret = pjp.proceed();
        cacheEntity(redisKey, ret);
        return ret;
    }

    @Around("execution(* com.wsmrxd.bloglite.mapping.CommentMapper.selectCommentByID(int)) && args(commentID)")
    public Object cacheCommentEntity(ProceedingJoinPoint pjp, int commentID) throws Throwable {
        String redisKey = "Comment::" + commentID;
        Comment commentCached = cacheService.keyVal().getValueByKey(redisKey);
        if(commentCached != null) return commentCached;

        Object ret = pjp.proceed();
        cacheEntity(redisKey, ret);
        return ret;
    }

    @Around("execution(* com.wsmrxd.bloglite.mapping.BlogMapper.selectTagNamesByBlogID(int)) && args(blogID)")
    public Object cacheBlogTags(ProceedingJoinPoint pjp, int blogID) throws Throwable {
        String redisKey = "TagNamesOfBlog::" + blogID;
        List<String> cachedTagNames = cacheService.keyVal().getValueByKey(redisKey);
        if(cachedTagNames != null) return cachedTagNames;

        Object ret = pjp.proceed();
        cacheEntity(redisKey, ret);
        return ret;
    }

    private void cacheEntity(String redisKey, @Nullable Object value) {
        if(value == null) return;
        cacheService.keyVal().setKeyValue(redisKey, value);
    }
}
