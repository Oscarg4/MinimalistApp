package com.example.minimalistapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddFragment : Fragment(), View.OnClickListener {
    private var productNameEditText: EditText? = null
    private var productDescriptionEditText: EditText? = null
    private var productPriceEditText: EditText? = null
    private var productImageTextView: TextView? = null
    private var selectImageButton : Button? = null
    private var addButton: Button? = null
    private var categorySpinner: Spinner? = null
    private var imageSelected : ImageView? = null
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    private val SELECT_PHOTO = 2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        productNameEditText = view.findViewById(R.id.productNameEditText)
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText)
        productPriceEditText = view.findViewById(R.id.productPriceEditText)
        productImageTextView = view.findViewById(R.id.productImageEditText)
        selectImageButton = view.findViewById(R.id.buttonSelectImage)
        imageSelected = view.findViewById(R.id.ImageSelected)
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

        selectImageButton!!.setOnClickListener {
            checkAndRequestPermissions()
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, SELECT_PHOTO)
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
        val imageString = productImageTextView!!.text.toString()

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

        if(imageString == ""){
            Toast.makeText(
                requireContext(),
                "Debe seleccionar una imagen",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val category = categorySpinner?.selectedItem.toString()

        val apiService = Conn.retrofit.create(ApiService::class.java)
        val product = Products(name = name, description = description, price = price, imageURL = imageString , category = category)

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
    fun convertBitmapToBase64(ivFoto : ImageView): String{
        val bitmap = (ivFoto.drawable as BitmapDrawable).bitmap
        var photoInBase64 = ""

        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            photoInBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        return photoInBase64
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val imageView: ImageView? = view?.findViewById(R.id.ImageSelected)
                imageView!!.setImageURI(uri)
                productImageTextView!!.text = convertBitmapToBase64(imageView)
            }
        }
    }
}
