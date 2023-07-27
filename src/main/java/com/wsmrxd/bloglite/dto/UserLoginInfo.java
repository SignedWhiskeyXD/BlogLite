package com.wsmrxd.bloglite.dto;

public class UserLoginInfo {
    private final String email;

    private final String password;

    public UserLoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserLoginInfo{" +
                "username='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
