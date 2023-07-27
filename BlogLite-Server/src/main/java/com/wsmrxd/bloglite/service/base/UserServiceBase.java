package com.wsmrxd.bloglite.service.base;

import com.wsmrxd.bloglite.entity.User;

public interface UserServiceBase {
    User getUser(int id);

    User getUser(String email);

    int queryUserID(String email);

    boolean changePassword(String email, String newPassword);

    boolean changeUsername(String email, String newUsername);

}
