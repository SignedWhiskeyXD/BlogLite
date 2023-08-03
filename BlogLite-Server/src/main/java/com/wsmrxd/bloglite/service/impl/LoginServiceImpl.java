package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.service.JWTService;
import com.wsmrxd.bloglite.service.LoginService;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private AuthenticationManager authManager;

    private JWTService jwtService;

    @Autowired
    public void setAuthManager(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Autowired
    public void setJwtService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public RestResponse doLogin(String email, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(email);

        return RestResponse.ok(new LoginSuccessInfo(email, jwt));
    }
}
