package com.wsmrxd.bloglite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Value("${myConfig.misc.nuxtDevPort:0}")
    private int nuxtDevPort;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/admin/**").authenticated()
                .anyRequest().permitAll();

        if(nuxtDevPort > 0)
            http.cors().configurationSource(corsConfigurationSource(nuxtDevPort));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(this.authEntryPoint);
    }

    // 由于Nuxt.js要用一套代码搞定服务端渲染和客户端渲染，在HMR之后，useFetch实际上由客户端调用，为此必须为开发环境启用CORS支持
    // 生产环境则可以忽略，因为生产环境不存在HMR，页面内容已经由Nuxt预渲染
    private CorsConfigurationSource corsConfigurationSource(int port) {
        CorsConfiguration configuration = new CorsConfiguration();
        String origin = "http://localhost:" + port;
        configuration.setAllowedOrigins(List.of(origin));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
