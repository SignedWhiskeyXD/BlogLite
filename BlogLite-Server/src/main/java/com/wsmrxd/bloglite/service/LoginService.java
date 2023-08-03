package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.vo.RestResponse;

public interface LoginService {
    RestResponse doLogin(String email, String password);
}
