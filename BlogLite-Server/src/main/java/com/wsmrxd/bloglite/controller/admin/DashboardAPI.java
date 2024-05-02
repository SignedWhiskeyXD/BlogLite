package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.vo.DashboardInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardAPI {

    @Deprecated
    @GetMapping
    public RestResponse<DashboardInfo> serveDashboardInfo(){
        DashboardInfo ret = new DashboardInfo();
        ret.setTotalUV(114514);
        return RestResponse.ok(ret);
    }
}
