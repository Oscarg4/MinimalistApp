package com.example.minimalistapp.model

data class Users(
    var id_Users: Int,
    var name: String,
    var surname: String,
    var email: String,
    var password: String,
    var admin: Boolean = false
)
