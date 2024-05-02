package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.RestResponse;
import com.wsmrxd.bloglite.vo.SiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/statistic")
public class SiteInfoAPI {

    @Autowired
    private SiteInfoService siteInfoService;

    @GetMapping
    public RestResponse<SiteInfo> serveSiteInfo() {
        return RestResponse.ok(siteInfoService.getSiteInfo());
    }

    @GetMapping("/rank")
    public RestResponse<List<BlogPreview>> serveBlogRanking() {
        return RestResponse.ok(siteInfoService.getBlogRanking());
    }
}
