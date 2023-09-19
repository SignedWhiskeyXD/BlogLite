package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.Utils.HttpUtil;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@RestController
@RequestMapping("/api/blog")
public class BlogAPI {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CacheService cacheService;

    @Value("${myConfig.blog.pvIncreaseCoolDownMinute}")
    private int pvCoolDownMinutesPerIP;

    @GetMapping("/{id}")
    public RestResponse<BlogDetail> serveBlogByID(@PathVariable int id, HttpServletRequest request){
        BlogDetail ret = blogService.getBlogDetail(id);
        if(ret == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");

        String clientRedisKey = "AccessBlog::" + id + "::" + HttpUtil.getIP(request);
        if(cacheService.keyVal().setKeyValueIfAbsent(clientRedisKey, " ", Duration.ofMinutes(pvCoolDownMinutesPerIP)))
            blogService.increaseBlogViews(id);

        ret.setViews(blogService.getBlogViewsAsCached(id));
        return RestResponse.ok(ret);
    }
}
