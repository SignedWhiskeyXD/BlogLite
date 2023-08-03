package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringBootTest
public class LoginTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Test
    public void testLogin(){
        var ret = loginService.doLogin("wsmrxd@gmail.com", "114514");
        System.out.println(ret.toString());
        LoginSuccessInfo info = (LoginSuccessInfo) ret.getResponseBody();
        System.out.println(info.getToken());
    }

    @Test
    public void testJWTVerify(){
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ3c21yeGRAZ21haWwuY29tIiwiaWF0IjoxNjkxMDQxNzMyLCJleHAiOjE2OTEzMDA5MzJ9.UBuTniDawkw7MgFHRInzqFFgLLSW9ssICtiLupAoaOw";
        System.out.println(jwtService.verifyToken(jwt));
        String userEmail = jwtService.getSubject(jwt);
        if (userEmail == null)
            throw new RuntimeException("Invalid Email");

        var authToken = new UsernamePasswordAuthenticationToken(userEmail, "114514", null);
        Authentication authentication = authManager.authenticate(authToken);

        System.out.println(userEmail);
        System.out.println(authentication.isAuthenticated());
    }
}
