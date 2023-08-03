package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.dto.UserLoginInfo;
import com.wsmrxd.bloglite.service.LoginService;
import com.wsmrxd.bloglite.service.impl.UserServiceImpl;
import com.wsmrxd.bloglite.service.JWTService;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public RestResponse serveLogin(@RequestBody UserLoginInfo body){
        return loginService.doLogin(body.getEmail(), body.getPassword());
    }
}
