package com.wsmrxd.bloglite.security;

import com.wsmrxd.bloglite.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

    public UserDetailsImpl(User userEntity, Collection<? extends GrantedAuthority> authorities){
        super(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }
}
