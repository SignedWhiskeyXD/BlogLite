package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.dto.GithubUser;

public interface GithubOAuthService {

    String getBearerTokenByAuthCode(String code);

    GithubUser getGithubUserInfo(String bearerToken);
}
