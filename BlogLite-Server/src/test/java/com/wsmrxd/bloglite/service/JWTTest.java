package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.service.base.JWTServiceBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTTest {

    @Autowired
    JWTServiceBase service;

    @Test
    public void testCreateJWT(){
        String jwt = service.generateToken("wsmrxd");
        assert (service.verifyToken(jwt));
        assert (!service.verifyToken("114514"));
    }

    @Test
    public void testSubject(){
        String jwt = service.generateToken("wsmrxd");
        System.out.println(service.getSubject(jwt));
    }
}
