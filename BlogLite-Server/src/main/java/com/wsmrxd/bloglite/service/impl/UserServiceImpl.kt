package com.wsmrxd.bloglite.service.impl

import com.wsmrxd.bloglite.entity.User
import com.wsmrxd.bloglite.exception.BlogAuthException
import com.wsmrxd.bloglite.mapping.UserMapper
import com.wsmrxd.bloglite.service.JWTService
import com.wsmrxd.bloglite.service.UserService
import com.wsmrxd.bloglite.vo.LoginSuccessInfo
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl
@Autowired constructor(
    private val mapper: UserMapper,
    private val jwtService: JWTService
) : UserService {

    override fun getUser(email: String): User? {
        return mapper.selectUserByEmail(email)
    }

    override fun doLogin(email: String, password: String): LoginSuccessInfo {
        val loginUser = getUser(email) ?: throw BlogAuthException("User not found!")

        if (!BCrypt.checkpw(password, loginUser.password)) throw BlogAuthException("Invalid user or password!")

        val jwt = jwtService.generateToken(email)
        return LoginSuccessInfo(email, jwt)
    }
}
