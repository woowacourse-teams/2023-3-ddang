package com.ddangddangddang.android.feature.register.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemSelectMainCategoryBinding

class MainCategoryAdapter(private val categories: List<String>) :
    RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder>() {
    class MainCategoryViewHolder(val binding: ItemSelectMainCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.category = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val binding = ItemSelectMainCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MainCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}
