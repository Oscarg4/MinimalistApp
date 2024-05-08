package com.example.minimalistapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.minimalistapp.model.Products
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var productsAdapter: ArrayAdapter<String>
    private var productsList = mutableListOf<String>()

    private val apiService: ApiService by lazy {
        Conn.retrofit.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        listView = view.findViewById(R.id.lista)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar el adaptador de productos
        productsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            productsList
        )
        listView.adapter = productsAdapter

        // Obtener los productos de la API
        obtenerProductos()
    }

    private fun obtenerProductos() {
        val call = apiService.obtenerTodosLosProductos()
        call.enqueue(object : Callback<List<Products>> {
            override fun onResponse(call: Call<List<Products>>, response: Response<List<Products>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        // Limpiar la lista y agregar los nuevos productos
                        productsList.clear()
                        for (product in products) {
                            productsList.add(product.name)
                        }
                        // Notificar al adaptador que los datos han cambiado
                        productsAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectarse al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
