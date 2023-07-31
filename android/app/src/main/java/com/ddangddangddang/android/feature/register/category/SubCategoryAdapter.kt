package com.ddangddangddang.android.feature.register.category

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.CategoryModel

class SubCategoryAdapter(
    private val onItemClickListener: (Long) -> Unit,
) : ListAdapter<CategoryModel, SubCategoryViewHolder>(
    CategoryDiffUtil(),
) {

    fun setCategories(categories: List<CategoryModel>) {
        submitList(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        return SubCategoryViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
