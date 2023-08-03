package com.wsmrxd.bloglite.util;

import com.wsmrxd.bloglite.mapping.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class SecurityTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testPasswordEncoding(){
        String myPassRaw = "114514";
        String myPassEncoded = userMapper.selectUserByEmail("wsmrxd@gmail.com").getPassword();
        var encoder = new BCryptPasswordEncoder();
        assert encoder.matches(myPassRaw, myPassEncoded);
    }
}
