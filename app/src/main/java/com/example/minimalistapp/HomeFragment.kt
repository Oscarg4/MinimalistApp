package com.example.minimalistapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Products
import com.example.minimalistapp.model.ProductsNew
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productAdapter = ProductAdapter(requireContext(), emptyList() )
        recyclerView.adapter = productAdapter

        obtenerProductos()
    }

    private fun obtenerProductos() {
        val apiService = Conn.retrofit.create(ApiService::class.java)

        apiService.obtenerTodosLosProductos().enqueue(object : Callback<List<ProductsNew>> {
            override fun onResponse(call: Call<List<ProductsNew>>, response: Response<List<ProductsNew>>) {
                if (response.isSuccessful) {
                    val products = response.body()
                    products?.let {
                        productAdapter.actualizarLista(it)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener productos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProductsNew>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de conexión al obtener productos.", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
