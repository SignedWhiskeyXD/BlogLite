package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.entity.User;
import com.wsmrxd.bloglite.mapping.UserMapper;
import com.wsmrxd.bloglite.security.UserDetailsImpl;
import com.wsmrxd.bloglite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    private UserMapper mapper;

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = mapper.selectUserByEmail(email);
        if(user == null)
            throw new UsernameNotFoundException("No User Found Under Such Username(email)");
        return new UserDetailsImpl(user, Collections.emptyList());
    }
}
