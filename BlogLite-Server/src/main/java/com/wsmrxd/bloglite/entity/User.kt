package com.wsmrxd.bloglite.entity;

data class User (
    var id: Int = 0,
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var role: String = ""
)