package com.wsmrxd.bloglite.service;

import javax.annotation.Nullable;

public interface JWTService {

    String extractTokenFromHeader(@Nullable String authorization);

    String generateToken(String subject);

    boolean verifyToken(String token);

    String getSubject(String token);
}
