package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.dto.BlogVisitorInfo;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.BlogService;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogDetail;
import com.wsmrxd.bloglite.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@RestController
@RequestMapping("/api/blog")
@Slf4j
public class BlogAPI {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SiteInfoService siteInfoService;

    @Value("${myConfig.blog.pvIncreaseCoolDownMinute}")
    private int pvCoolDownMinutesPerIP;

    @GetMapping("/{id}")
    public RestResponse<BlogDetail> serveBlogByID(@PathVariable int id, HttpServletRequest request){
        BlogDetail ret = blogService.getBlogDetail(id);
        if(ret == null)
            throw new BlogException(ErrorCode.BLOG_NOT_FOUND, "No Such Blog!");

        ret.setViews(blogService.getBlogViewsAsCached(id));
        return RestResponse.ok(ret);
    }

    @PostMapping("/views")
    public RestResponse<String> addBlogViews(@RequestBody BlogVisitorInfo visitorInfo) {
        int blogID = visitorInfo.getBlogID();
        String ipAddr = visitorInfo.getIpAddr();
        String clientRedisKey = "Access::" + blogID + "::" + ipAddr;

        if(cacheService.keyVal().setKeyValueIfAbsent(clientRedisKey, " ", Duration.ofMinutes(pvCoolDownMinutesPerIP))) {
            siteInfoService.increaseBlogViews(blogID);
            log.info("Visitor from {} accessed blog {}", ipAddr, blogID);
        }

        return RestResponse.ok("OK");
    }
}
