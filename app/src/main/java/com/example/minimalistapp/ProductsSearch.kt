package com.example.minimalistapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimalistapp.model.Products

class ProductsSearch(private val context: Context, private val products: List<Products>) : RecyclerView.Adapter<ProductsSearch.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Products) {
            itemView.findViewById<TextView>(R.id.productNameTextView).text = product.name
            itemView.findViewById<TextView>(R.id.productDescriptionTextView).text = product.description
            itemView.findViewById<TextView>(R.id.productPriceTextView).text = product.price.toString()
        }
    }

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
}
