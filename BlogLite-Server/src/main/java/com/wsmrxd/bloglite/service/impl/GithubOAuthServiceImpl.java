package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.dto.GithubUser;
import com.wsmrxd.bloglite.service.GithubOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GithubOAuthServiceImpl implements GithubOAuthService {

    @Value("${myConfig.OAuth2.Github.client_id}")
    private String client_id;

    @Value("${myConfig.OAuth2.Github.client_secret}")
    private String client_secret;

    @Value("${myConfig.OAuth2.Github.redirect_uri}")
    private String redirect_uri;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getBearerTokenByAuthCode(String code) {
        String baseURI = "https://github.com/login/oauth/access_token";

        var uri = UriComponentsBuilder.fromHttpUrl(baseURI)
                .queryParam("client_id", client_id)
                .queryParam("client_secret", client_secret)
                .queryParam("redirect_uri", redirect_uri)
                .queryParam("code", code)
                .build().toUri();

        String response = restTemplate.getForObject(uri, String.class);
        if(response == null) return "";

        var fields = response.split("&");
        for(String field : fields){
            if(!field.startsWith("access_token=")) continue;

            int index = field.indexOf('=');
            return field.substring(index + 1);
        }
        return "";
    }

    @Override
    public GithubUser getGithubUserInfo(String bearerToken) {
        String userURI = "https://api.github.com/user";
        String emailURI = "https://api.github.com/user/emails";
        String token = "Bearer " + bearerToken;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            var response = restTemplate.exchange(userURI, HttpMethod.GET, requestEntity, GithubUser.class);
            return response.getBody();
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
