package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.dto.UserLoginInfo;
import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.service.UserService;
import com.wsmrxd.bloglite.service.base.JWTServiceBase;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private UserService userService;

    private JWTServiceBase jwtService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtService(JWTServiceBase jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping
    public RestResponse serveLogin(@RequestBody UserLoginInfo body){
        var targetUser = userService.getUser(body.getEmail());
        if(targetUser == null)
            throw new BlogException(ErrorCode.USER_NOT_FOUND, "No Such User");

        if(targetUser.getPassword().equals(body.getPassword())) {
            String jwt = jwtService.generateToken(body.getEmail());
            var ret = new LoginSuccessInfo(body.getEmail(), jwt);
            return RestResponse.ok(ret);
        }
        else
            throw new BlogException(ErrorCode.INVALID_PASSWORD, "Invalid Password");
    }
}
