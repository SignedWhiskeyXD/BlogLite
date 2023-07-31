package com.wsmrxd.bloglite.service.base;

public interface JWTServiceBase {

    String generateToken(String subject);

    boolean verifyToken(String token);

    String getSubject(String token);

}
