package com.example.minimalistapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Products
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

        // Pasar el contexto y una lista vacía al crear una instancia de ProductAdapter
        productAdapter = ProductAdapter(requireContext(), emptyList())
        recyclerView.adapter = productAdapter

        // Llamar al método para obtener los productos
        obtenerProductos()
    }

    private fun obtenerProductos() {
        val apiService = Conn.retrofit.create(ApiService::class.java)

        apiService.obtenerTodosLosProductos().enqueue(object : Callback<List<Products>> {
            override fun onResponse(call: Call<List<Products>>, response: Response<List<Products>>) {
                if (response.isSuccessful) {
                    // Actualizar el adaptador con la lista de productos obtenida
                    val products = response.body()
                    products?.let {
                        productAdapter.actualizarLista(it)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener productos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de conexión al obtener productos.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}