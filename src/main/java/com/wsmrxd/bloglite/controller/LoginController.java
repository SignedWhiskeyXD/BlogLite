package com.wsmrxd.bloglite.controller;

import com.wsmrxd.bloglite.dto.UserLoginInfo;
import com.wsmrxd.bloglite.entity.User;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.UserService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    UserService service;

    @PostMapping
    public RestResponse serveLogin(@RequestBody UserLoginInfo body){
        var targetUser = service.getUser(body.getEmail());
        if(targetUser == null)
            throw new BlogException(ErrorCode.USER_NOT_FOUND, "No Such User");

        if(targetUser.getPassword().equals(body.getPassword()))
            return RestResponse.ok("Success");
        else
            throw new BlogException(ErrorCode.INVALID_PASSWORD, "Invalid Password");
    }
}
