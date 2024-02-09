package com.example.minimalistapp.model

import java.io.Serializable

data class Users(
    var id: Int = 0,
    var name: String,
    var surname: String,
    var username: String,
    var email: String,
    var password: String,
    var admin: Boolean = false,
    var baja: Boolean = false
) : Serializable
