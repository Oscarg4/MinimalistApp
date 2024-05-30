package com.example.minimalistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.minimalistapp.Retrofit.ApiService
import com.example.minimalistapp.Retrofit.Conn
import com.example.minimalistapp.model.Products
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFragment : Fragment(), View.OnClickListener {
    private var productNameEditText: EditText? = null
    private var productDescriptionEditText: EditText? = null
    private var productPriceEditText: EditText? = null
    private var addButton: Button? = null
    private var categorySpinner: Spinner? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        productNameEditText = view.findViewById(R.id.productNameEditText)
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText)
        productPriceEditText = view.findViewById(R.id.productPriceEditText)
        addButton = view.findViewById(R.id.addButton)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        addButton?.setOnClickListener(this)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner?.adapter = adapter
        }

        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.addButton -> agregarProducto()
        }
    }

    private fun agregarProducto() {
        val name = productNameEditText!!.text.toString()
        val description = productDescriptionEditText!!.text.toString()
        val priceString = productPriceEditText!!.text.toString()

        if (name.isEmpty() || description.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Por favor, complete todos los campos.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val price = try {
            priceString.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(
                requireContext(),
                "El precio ingresado no es válido.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val category = categorySpinner?.selectedItem.toString()

        val apiService = Conn.retrofit.create(ApiService::class.java)
        val product = Products(name = name, description = description, price = price, category = category)

        val call = apiService.agregarProducto(product)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(), "Producto agregado correctamente.", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(), "Error al agregar el producto: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    requireContext(), "Error de conexión al agregar el producto.", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
