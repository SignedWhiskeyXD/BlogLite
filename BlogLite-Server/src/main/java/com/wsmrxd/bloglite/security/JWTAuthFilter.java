package com.wsmrxd.bloglite.security;

import com.wsmrxd.bloglite.service.impl.UserServiceImpl;
import com.wsmrxd.bloglite.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    JWTService jwtService;

    UserServiceImpl userServiceImpl;

    @Autowired
    public void setJwtService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = jwtService.extractTokenFromHeader(authHeader);

        if(jwt == null){
            filterChain.doFilter(request, response);
            return;
        }

        if(!jwtService.verifyToken(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        String userEmail = jwtService.getSubject(jwt);
        if (userEmail == null)
            throw new RuntimeException("Invalid Email");

        // TODO: 从缓存检测是否登录

        var authToken = new UsernamePasswordAuthenticationToken(userEmail, "114514", null);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        if(authToken.isAuthenticated())
            System.out.println("JWT verified");
        filterChain.doFilter(request, response);
    }
}
