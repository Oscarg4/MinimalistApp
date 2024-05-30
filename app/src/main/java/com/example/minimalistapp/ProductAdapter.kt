package com.example.minimalistapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimalistapp.model.Products

class ProductAdapter(private val context: Context, private var products: List<Products>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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
    interface OnFavoriteIconClickListener {
        fun onFavoriteIconClick(product: Products)
    }


    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val productDescriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val productCategoryTextView: TextView = itemView.findViewById(R.id.categoryNameTextView) // TextView para la categoría

        fun bind(product: Products) {
            productNameTextView.text = product.name
            productDescriptionTextView.text = product.description
            productPriceTextView.text = product.price.toString()
            productCategoryTextView.text = product.category // Mostrar el nombre de la categoría
        }
    }


    fun actualizarLista(nuevaLista: List<Products>) {
        products = nuevaLista
        notifyDataSetChanged()
    }
}