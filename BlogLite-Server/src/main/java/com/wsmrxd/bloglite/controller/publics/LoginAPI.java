package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.dto.UserLoginInfo;
import com.wsmrxd.bloglite.service.LoginService;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginAPI {

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public RestResponse<LoginSuccessInfo> serveLogin(@RequestBody UserLoginInfo body){
        LoginSuccessInfo ret = loginService.doLogin(body.getEmail(), body.getPassword());
        return RestResponse.ok(ret);
    }
}
