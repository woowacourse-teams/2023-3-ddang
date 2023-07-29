package com.ddangddangddang.android.feature.register.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.databinding.ItemSelectSubCategoryBinding
import com.ddangddangddang.android.model.CategoriesModel

class SubCategoryAdapter(
    private val onItemClickListener: (Long) -> Unit,
) : ListAdapter<CategoriesModel.CategoryModel, SubCategoryViewHolder>(
    CategoryDiffUtil(),
) {

    fun setCategories(categories: List<CategoriesModel.CategoryModel>) {
        submitList(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding = ItemSelectSubCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return SubCategoryViewHolder(binding, onItemClickListener)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
