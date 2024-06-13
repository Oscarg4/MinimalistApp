package com.example.minimalistapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Products
import com.example.minimalistapp.model.ProductsNew
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream

class DescriptionActivity : AppCompatActivity() {

    private lateinit var titleDesc: TextView
    private lateinit var descriptionDesc: TextView
    private lateinit var priceDesc: TextView
    private lateinit var categoryDesc: TextView
    private lateinit var image: ImageView

    private lateinit var productsList : List<ProductsNew>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        productsList = emptyList()
        val productId = intent.getIntExtra("id", 0)
        Log.e("resultado", productId.toString())
        obtenerProducto(productId)

    }

    private fun obtenerProducto (id : Int) {
        val apiService = Conn.retrofit.create(ApiService::class.java)

        apiService.obtenerProducto(id).enqueue(object : Callback<List<ProductsNew>> {
            override fun onResponse(call: Call<List<ProductsNew>>, response: Response<List<ProductsNew>>) {
                if (response.isSuccessful) {
                    val products = response.body()

                    titleDesc = findViewById(R.id.name)
                    descriptionDesc = findViewById(R.id.descrption)
                    priceDesc = findViewById(R.id.price)
                    categoryDesc = findViewById(R.id.category)
                    image = findViewById(R.id.image)

                    titleDesc.text = products!!.first().name
                    descriptionDesc.text = products!!.first().description
                    priceDesc.text = products!!.first().price.toString()
                    categoryDesc.text = products!!.first().category

                    val decodedBytes = Base64.decode(products!!.first().imageURL, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
                    image.setImageBitmap(bitmap)

                } else {
                    Toast.makeText(this@DescriptionActivity, "Error al obtener productos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProductsNew>>, t: Throwable) {
                Toast.makeText(this@DescriptionActivity, "Error de conexión al obtener productos.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
