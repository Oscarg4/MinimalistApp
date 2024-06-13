package com.example.minimalistapp.Retrofit

import com.example.minimalistapp.model.Products
import com.example.minimalistapp.model.ProductsNew
import com.example.minimalistapp.model.Users
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Api de Usuarios
    @GET("users")
    fun obtenerTodosLosUsers(): Call<List<Users>>

    @POST("users/add")
    fun addUsers(@Body users: Users): Call<Void>

    @PUT("users/update/{userId}")
    fun actualizarUser(@Body users: Users, @Path("userId") userId: Int): Call<Void>

    // Eliminar un usuario por ID
    @DELETE("users/delete/{userId}")
    fun eliminarUsers(@Path("userId") userId: Long): Call<Void>

    @GET("users/baja")
    fun obtenerTodosLosUsersBaja(): Call<List<Users>>

    @GET("users/login")
    fun buscarUsuarioPorNombreYContraseña(@Query("name") name: String, @Query("password") password: String): Call<Users>

    @GET("users/{userId}")
    fun buscarUsuarioPorId(@Path("userId") userId: Int): Call<Users>

    // Api de los productos
    @GET("products")
    fun obtenerTodosLosProductos(): Call<List<ProductsNew>>

    @GET("products/{id}")
    fun obtenerProducto(@Path("id") id : Int): Call<List<ProductsNew>>

    @POST("products/add")
    fun agregarProducto(@Body product: Products): Call<Void>


    // Actualizar un producto existente
    @PUT("products/update/{productId}")
    fun actualizarProducto(@Body producto: Products, @Path("productId") productId: Long): Call<Void>

    // Eliminar un producto por ID
    @DELETE("products/delete/{productId}")
    fun eliminarProducto(@Path("productId") productId: Long): Call<Void>


}
