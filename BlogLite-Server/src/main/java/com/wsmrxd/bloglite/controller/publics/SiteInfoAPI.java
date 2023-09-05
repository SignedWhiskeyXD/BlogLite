package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.service.SiteInfoService;
import com.wsmrxd.bloglite.vo.BlogPreview;
import com.wsmrxd.bloglite.vo.RestResponse;
import com.wsmrxd.bloglite.vo.SiteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class SiteInfoAPI {

    private SiteInfoService siteInfoService;

    @Autowired
    public void setSiteInfoService(SiteInfoService siteInfoService) {
        this.siteInfoService = siteInfoService;
    }

    @GetMapping
    public RestResponse<SiteInfo> serveSiteInfo(){
        return RestResponse.ok(siteInfoService.getSiteInfo());
    }

    @GetMapping("/rank")
    public RestResponse<List<BlogPreview>> serveBlogRanking(){
        return RestResponse.ok(siteInfoService.getBlogRanking());
    }
}
