package top.whiskeyxd.blogliteauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.whiskeyxd.blogliteauth.dto.LoginInfo;
import top.whiskeyxd.blogliteauth.entity.User;
import top.whiskeyxd.blogliteauth.service.UserService;
import top.whiskeyxd.bloglitecommon.enums.ErrorCode;
import top.whiskeyxd.bloglitecommon.exception.BlogLiteException;
import top.whiskeyxd.bloglitecommon.jwt.JWTUtils;
import top.whiskeyxd.bloglitecommon.vo.RestResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiAuth/login")
public class LoginAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping
    public RestResponse<Map<String, String>> doLogin(@RequestBody LoginInfo loginRequest){
        User targetUser = userService.findUserByUsername(loginRequest.getUsername());
        if(targetUser == null)
            throw new BlogLiteException(ErrorCode.USER_NOT_FOUND);

        if(!passwordEncoder.matches(loginRequest.getPassword(), targetUser.getPassword()))
            throw new BlogLiteException(ErrorCode.AUTHORIZE_FAILED);

        Map<String, String> ret = new HashMap<>();
        ret.put("token", jwtUtils.generateToken(loginRequest.getUsername()));
        return RestResponse.ok(ret);
    }
}
