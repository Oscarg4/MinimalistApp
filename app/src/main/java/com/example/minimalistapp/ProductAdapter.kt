package com.example.minimalistapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimalistapp.model.ProductsNew
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ProductAdapter(private val context: Context, private var products: List<ProductsNew>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sharedPreferences = SharedPreferences(itemView.context)
        private val productImage: ImageView = itemView.findViewById(R.id.imageProduct)
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val productDescriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val productCategoryTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        private val favoriteImageView : ImageView = itemView.findViewById(R.id.favoriteImageView)
        private var fav = false

        fun bind(product: ProductsNew) {
            val decodedBytes = Base64.decode(product.imageURL, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
            productImage.setImageBitmap(bitmap)
            productNameTextView.text = product.name
            productDescriptionTextView.text = product.description
            productPriceTextView.text = product.price.toString()
            productCategoryTextView.text = product.category

            if (product.favorite == true) {
                favoriteImageView.setImageResource(R.drawable.ic_favorite_filled)
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DescriptionActivity::class.java)
                intent.putExtra("id", product.id_Products)
                context.startActivity(intent)
            }

            favoriteImageView.setOnClickListener {
                if (product.favorite == false){
                    favoriteImageView.setImageResource(R.drawable.ic_favorite_filled)
                    sharedPreferences.saveFavorites(product)
                    product.favorite = true
                } else {
                    favoriteImageView.setImageResource(R.drawable.ic_favorite_empty)
                    sharedPreferences.deleteFavorite(product)
                    product.favorite = false
                    fav = false
                }
            }
        }
    }
    fun actualizarLista(nuevaLista: List<ProductsNew>) {
        products = nuevaLista
        notifyDataSetChanged()
    }
}
