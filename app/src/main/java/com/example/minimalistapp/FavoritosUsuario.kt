package com.example.minimalistapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minimalistapp.model.Products

class FavoritosUsuario : Fragment(R.layout.fragment_favoritos_usuario) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = SharedPreferences(requireContext())

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productAdapter = ProductAdapter(requireContext(), emptyList() )
        recyclerView.adapter = productAdapter

        productAdapter.actualizarLista(sharedPreferences.getFavorites())

    }
}
