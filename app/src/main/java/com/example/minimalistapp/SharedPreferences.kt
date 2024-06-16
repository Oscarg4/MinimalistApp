package com.example.minimalistapp

import android.content.Context
import android.content.SharedPreferences
import com.example.minimalistapp.model.ProductsNew
import com.example.minimalistapp.model.Users
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferences(context: Context) {


    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private var favs = ArrayList<ProductsNew>()
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
    fun getFavorites() : List<ProductsNew>{
        val favorites = sharedPreferences.getString("favorites", null)
        if(favorites != null) {
            return gson.fromJson(favorites, object : TypeToken<List<ProductsNew>>() {}.type)
        } else {
            return emptyList()
        }
    }

    fun saveFavorites(product : ProductsNew){
        favs = ArrayList(getFavorites())
        favs.add(product)
        val editor = sharedPreferences.edit()
        val favoritesJson = gson.toJson(favs)
        editor.putString("favorites", favoritesJson)
        editor.apply()
    }

    fun deleteFavorite(product: ProductsNew) {
        favs = ArrayList(getFavorites())

        favs.remove(favs.find { it.id_Products == product.id_Products })
        val editor = sharedPreferences.edit()
        val favRestaurantsJson = gson.toJson(favs)
        editor.putString("favorites", favRestaurantsJson)
        editor.apply()
    }


}