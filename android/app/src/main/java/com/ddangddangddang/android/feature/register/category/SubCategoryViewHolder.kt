package com.ddangddangddang.android.feature.register.category

import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemSelectSubCategoryBinding
import com.ddangddangddang.android.model.CategoriesModel

class SubCategoryViewHolder(
    private val binding: ItemSelectSubCategoryBinding,
    onItemClickListener: (Long) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClickListener
    }

    fun bind(category: CategoriesModel.CategoryModel) {
        binding.category = category
    }
}
