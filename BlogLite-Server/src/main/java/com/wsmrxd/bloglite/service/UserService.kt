package com.wsmrxd.bloglite.service

import com.wsmrxd.bloglite.entity.User
import com.wsmrxd.bloglite.vo.LoginSuccessInfo

interface UserService {
    fun getUser(email: String): User?

    fun doLogin(email: String, password: String): LoginSuccessInfo
}
