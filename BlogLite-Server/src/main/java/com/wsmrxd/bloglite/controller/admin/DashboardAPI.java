package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.service.CacheService;
import com.wsmrxd.bloglite.vo.DashboardInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardAPI {

    private static final String SITE_PV_KEY = "SitePV";

    @Autowired
    private CacheService cacheService;

    @GetMapping
    public RestResponse<DashboardInfo> serveDashboardInfo(){
        DashboardInfo ret = new DashboardInfo();
        ret.setTotalUV(cacheService.hyperLogLog().count(SITE_PV_KEY));
        return RestResponse.ok(ret);
    }
}
