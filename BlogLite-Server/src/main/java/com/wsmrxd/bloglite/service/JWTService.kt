package com.wsmrxd.bloglite.service;

interface JWTService {

    fun extractTokenFromHeader(authorization: String?): String

    fun generateToken(subject: String): String

    fun verifyToken(token: String): Boolean
}