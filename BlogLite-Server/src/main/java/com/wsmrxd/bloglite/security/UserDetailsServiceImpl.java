package com.wsmrxd.bloglite.security;

import com.wsmrxd.bloglite.mapping.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserMapper mapper;

    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = mapper.selectUserByEmail(email);
        if(user == null)
            throw new UsernameNotFoundException("Cannot find user under such email!");

        return new UserDetailsImpl(user);
    }
}
