package com.wsmrxd.bloglite.service.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.wsmrxd.bloglite.service.JWTService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JWTServiceImpl(
    @Value("\${myConfig.jwt.expireTime}")
    private val expireTime: Long,

    @Value("\${myConfig.jwt.secretKey}")
    private val secretKey: String
) : JWTService {

    private val algorithm: Algorithm = Algorithm.HMAC256(secretKey)

    override fun extractTokenFromHeader(authorization: String?): String {
        return if (authorization?.startsWith("Bearer ") == true)
            authorization.substring(7)
        else
            ""
    }

    override fun generateToken(subject: String): String {
        val currentTileMsecs = System.currentTimeMillis()
        val issuedAt = Date(currentTileMsecs)
        val expireAt = Date(currentTileMsecs + expireTime)

        return JWT.create().withSubject(subject)
            .withIssuedAt(issuedAt)
            .withExpiresAt(expireAt)
            .sign(algorithm)
    }

    override fun verifyToken(token: String): Boolean {
        return try {
            JWT.require(algorithm).build().verify(token)
            true
        } catch (e: JWTVerificationException) {
            false
        }
    }
}
