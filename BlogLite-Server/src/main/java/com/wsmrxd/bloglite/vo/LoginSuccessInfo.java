package com.wsmrxd.bloglite.vo;

public class LoginSuccessInfo {
    private final String email;

    private final String token;

    public LoginSuccessInfo(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
