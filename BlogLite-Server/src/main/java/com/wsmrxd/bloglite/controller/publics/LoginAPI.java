package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.dto.UserLoginInfo;
import com.wsmrxd.bloglite.service.UserService;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginAPI {

    @Autowired
    private UserService userService;

    @PostMapping
    public RestResponse<LoginSuccessInfo> serveLogin(@RequestBody UserLoginInfo body){
        LoginSuccessInfo ret = userService.doLogin(body.getEmail(), body.getPassword());
        return RestResponse.ok(ret);
    }
}
