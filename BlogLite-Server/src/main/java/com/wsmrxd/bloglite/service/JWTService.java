package com.wsmrxd.bloglite.service;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JWTService {
    String extractTokenFromHeader(String authorization);

    String generateToken(String subject);

    boolean verifyToken(String token);

    String getSubject(String token);

}
