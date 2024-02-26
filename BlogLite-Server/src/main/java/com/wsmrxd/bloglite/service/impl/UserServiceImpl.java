package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.entity.User;
import com.wsmrxd.bloglite.exception.BlogAuthException;
import com.wsmrxd.bloglite.mapping.UserMapper;
import com.wsmrxd.bloglite.service.JWTService;
import com.wsmrxd.bloglite.service.UserService;
import com.wsmrxd.bloglite.vo.LoginSuccessInfo;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private JWTService jwtService;

    @Override
    public User getUser(int id) {
        return mapper.selectUserByID(id);
    }

    @Override
    public User getUser(String email) {
        return mapper.selectUserByEmail(email);
    }

    @Override
    public int queryUserID(String email) {
        return mapper.selectUserByEmail(email).getId();
    }

    @Override
    public boolean changePassword(String email, String newPassword) {
        return mapper.updateUserPassword(email, newPassword);
    }

    @Override
    public boolean changeUsername(String email, String newUsername) {
        return mapper.updateUserUsername(email, newUsername);
    }

    @Override
    public LoginSuccessInfo doLogin(String email, String password) {
        User loginUser = getUser(email);
        if (loginUser == null)
            throw new BlogAuthException("User not found!");

        if(!BCrypt.checkpw(password, loginUser.getPassword()))
            throw new BlogAuthException("Invalid user or password!");

        String jwt = jwtService.generateToken(email);
        return new LoginSuccessInfo(email, jwt);
    }
}
