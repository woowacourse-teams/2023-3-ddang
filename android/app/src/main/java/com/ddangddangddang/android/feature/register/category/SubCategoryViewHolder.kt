package com.ddangddangddang.android.feature.register.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemSelectSubCategoryBinding
import com.ddangddangddang.android.model.CategoryModel

class SubCategoryViewHolder(
    private val binding: ItemSelectSubCategoryBinding,
    onItemClickListener: (Long) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClickListener
    }

    fun bind(category: CategoryModel) {
        binding.category = category
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): SubCategoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSelectSubCategoryBinding.inflate(layoutInflater, parent, false)
            return SubCategoryViewHolder(binding, onItemClick)
        }
    }
}
