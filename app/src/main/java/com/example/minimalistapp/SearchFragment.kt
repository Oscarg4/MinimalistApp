package com.example.minimalistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
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

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var spinner: Spinner
    private lateinit var productsAdapter: ProductAdapter
    private var productsList = mutableListOf<ProductsNew>()
    private var filteredProductsList = mutableListOf<ProductsNew>()

    private val apiService: ApiService by lazy {
        Conn.retrofit.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.lista)
        searchView = view.findViewById(R.id.searchView)
        spinner = view.findViewById(R.id.spinner)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productsAdapter = ProductAdapter(requireContext(), filteredProductsList)
        recyclerView.adapter = productsAdapter

        // Configurar el listener para el campo de búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filtrarProductosPorNombre(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarProductosPorNombre(newText.orEmpty())
                return true
            }
        })

        // Configurar el Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val category = parent.getItemAtPosition(position).toString()
                    filtrarProductosPorCategoria(category)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No hacer nada
                }
            }
        }

        // Obtener los productos de la API
        obtenerProductos()
    }

    private fun obtenerProductos() {
        val call = apiService.obtenerTodosLosProductos()
        call.enqueue(object : Callback<List<ProductsNew>> {
            override fun onResponse(call: Call<List<ProductsNew>>, response: Response<List<ProductsNew>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        // Limpiar la lista y agregar los nuevos productos
                        productsList.clear()
                        productsList.addAll(products)
                        // Copiar productos a la lista filtrada inicialmente
                        filteredProductsList.clear()
                        filteredProductsList.addAll(products)
                        // Notificar al adaptador que los datos han cambiado
                        productsAdapter.actualizarLista(filteredProductsList)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProductsNew>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectarse al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrarProductosPorNombre(query: String) {
        filteredProductsList.clear()
        if (query.isEmpty()) {
            filteredProductsList.addAll(productsList)
        } else {
            val filteredProducts = productsList.filter { it.name.contains(query, ignoreCase = true) }
            filteredProductsList.addAll(filteredProducts)
        }
        productsAdapter.actualizarLista(filteredProductsList)
    }

    private fun filtrarProductosPorCategoria(category: String) {
        filteredProductsList.clear()
        if (category == "Todos") {
            filteredProductsList.addAll(productsList)
        } else {
            val filteredProducts = productsList.filter { it.category == category }
            filteredProductsList.addAll(filteredProducts)
        }
        productsAdapter.actualizarLista(filteredProductsList)
    }
}