package com.example.minimalistapp.model

data class Products(
    val id_Products: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val imageBytes: ByteArray? // Atributo para almacenar los bytes de la imagen
)
