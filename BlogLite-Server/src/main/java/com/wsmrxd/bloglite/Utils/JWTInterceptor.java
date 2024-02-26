package com.wsmrxd.bloglite.Utils;

import com.wsmrxd.bloglite.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = jwtService.extractTokenFromHeader(request.getHeader("Authorization"));

        if(token.isEmpty() || !jwtService.verifyToken(token)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
