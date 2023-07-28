package com.ddangddangddang.android.feature.register.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemSelectSubCategoryBinding

class SubCategoryAdapter(private val categories: List<String>) :
    RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {
    class SubCategoryViewHolder(val binding: ItemSelectSubCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.category = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding = ItemSelectSubCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return SubCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}
