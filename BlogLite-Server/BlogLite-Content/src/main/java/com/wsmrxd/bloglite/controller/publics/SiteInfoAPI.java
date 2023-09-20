package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.Utils.HttpUtil;
import com.wsmrxd.bloglite.entity.VisitorLog;
import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.RestResponse;
import com.wsmrxd.bloglite.vo.SiteInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/statistic")
public class SiteInfoAPI {

    @Autowired
    private SiteInfoService siteInfoService;

    @Autowired
    private CacheService  cacheService;

    private static final String SITE_PV_KEY = "SitePV";

    @GetMapping
    public RestResponse<SiteInfo> serveSiteInfo(){
        return RestResponse.ok(siteInfoService.getSiteInfo());
    }

    @GetMapping("/rank")
    public RestResponse<List<BlogPreview>> serveBlogRanking(){
        return RestResponse.ok(siteInfoService.getBlogRanking());
    }

    @PostMapping("/siteUV")
    public RestResponse<Object> addSiteUV(HttpServletRequest request){
        // 目前来说，统计UV的策略就是IP + 设备类型 + 操作系统 + 浏览器，共同组成了客户端指纹
        // TODO: 说实话这个日志方案不太行，之后要改为AOP实现
        VisitorLog visitor = HttpUtil.parseVisitor(request);
        log.info("Visitor from {}, {}, {}, {}",
                visitor.getIp(), visitor.getDevice(), visitor.getOs(), visitor.getBrowser());
        cacheService.hyperLogLog().add(SITE_PV_KEY, visitor.toString());
        return RestResponse.ok();
    }
}
