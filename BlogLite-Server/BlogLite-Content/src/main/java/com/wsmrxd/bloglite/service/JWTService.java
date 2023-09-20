package com.wsmrxd.bloglite.service;

public interface JWTService {
    String extractTokenFromHeader(String authorization);

    String generateToken(String subject);

    boolean verifyToken(String token);

    String getSubject(String token);

}
