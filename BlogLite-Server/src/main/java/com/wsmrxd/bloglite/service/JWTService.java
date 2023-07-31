package com.wsmrxd.bloglite.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wsmrxd.bloglite.service.base.JWTServiceBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService implements JWTServiceBase {

    private String secretKey;

    private long expireTime;

    @Value("${myConfig.jwt.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${myConfig.jwt.expireTime}")
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String generateToken(String subject) {
        long currentTileMsecs = System.currentTimeMillis();
        Date issuedAt = new Date(currentTileMsecs);
        Date expireAt = new Date(currentTileMsecs + expireTime);
        var algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create().withSubject(subject)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expireAt)
                .sign(algorithm);
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm).build().verify(token);

        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    @Override
    public String getSubject(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            var decodedJWT = JWT.decode(token);
            return decodedJWT.getSubject();
        }catch (JWTVerificationException e){
            return null;
        }
    }

}
