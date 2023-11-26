package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ListItemBinding
import com.example.myapplication.entity.Product

class ProductAdapter:ListAdapter<Product, ProductAdapter.Holder>(Comparator()) {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        private val binding = ListItemBinding.bind(view)

        fun bind(product: Product) = with(binding){
            itemTitle.text = product.title
            itemDescriptions.text = product.description
        }
    }

    class Comparator : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}