package com.wsmrxd.bloglite.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService service;

    @Test
    public void test(){
        var user = service.getUser(1);
        var result = service.changePassword(user.getEmail(), "1919810");
        System.out.println(result);
    }
}
