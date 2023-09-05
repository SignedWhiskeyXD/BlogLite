package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import com.wsmrxd.bloglite.vo.RestResponse;

public interface LoginService {
    LoginSuccessInfo doLogin(String email, String password);
}
