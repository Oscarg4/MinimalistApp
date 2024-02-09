package com.example.minimalistapp.Retrofit

import com.example.minimalistapp.model.Users
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    fun obtenerTodosLosUsers(): Call<List<Users>>

    @POST("users/add")
    fun addUsers(@Body users: Users): Call<Void>

    @PUT("users/update")
    fun actualizarUser(@Body users: Users): Call<Void>

    @DELETE("users/delete/{usersId}")
    fun eliminarUsers(@Path("usersId") usersId: Long): Call<Void>

    @GET("users/baja")
    fun obtenerTodosLosUsersBaja(): Call<List<Users>>

    @GET("users/login")
    fun buscarUsuarioPorNombreYContraseña(@Query("name") name: String, @Query("password") password: String): Call<Users>
}
