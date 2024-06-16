package com.example.minimalistapp.model



data class ProductsNew(
    val id_Products: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String,
    val imageURL: String,
    var favorite: Boolean = false
)


