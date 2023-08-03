package com.wsmrxd.bloglite.security;

import com.wsmrxd.bloglite.service.UserService;
import com.wsmrxd.bloglite.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    UserService userService;

    UserDetailsService userDetailsService;

    @Autowired
    public void setJwtService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = jwtService.extractTokenFromHeader(authHeader);

        // 任何一个条件被否决都会跳出， 即JWT鉴权失败
        if(jwt != null && jwtService.verifyToken(jwt)){
            String userEmail = jwtService.getSubject(jwt);
            if(userEmail != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                var authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
