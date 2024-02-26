package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.entity.User;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;

public interface UserService {
    User getUser(int id);

    User getUser(String email);

    int queryUserID(String email);

    boolean changePassword(String email, String newPassword);

    boolean changeUsername(String email, String newUsername);

    LoginSuccessInfo doLogin(String email, String password);
}
