package com.ddangddangddang.android.feature.register.category

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.CategoryModel

class MainCategoryAdapter(
    private val onItemClickListener: (Long) -> Unit,
) : ListAdapter<CategoryModel, MainCategoryViewHolder>(CategoryDiffUtil()) {

    fun setCategories(categories: List<CategoryModel>) {
        submitList(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        return MainCategoryViewHolder.create(parent, onItemClickListener)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
