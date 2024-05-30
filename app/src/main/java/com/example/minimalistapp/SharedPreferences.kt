package com.example.minimalistapp

import android.content.Context
import android.content.SharedPreferences
import com.example.minimalistapp.model.Users
import com.google.gson.Gson

class SharedPreferences(context: Context) {

    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addUser(user: Users){
        val editor = sharedPreferences.edit()
        val json = gson.toJson(user)
        editor.putString("user", json)
        editor.apply()
    }

    fun getUser(): Users? {
        val json = sharedPreferences.getString("user", null)
        return if (json != null){
            gson.fromJson(json, Users::class.java)
        } else {
            null
        }
    }
    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


}