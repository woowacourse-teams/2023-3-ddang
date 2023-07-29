package com.ddangddangddang.android.feature.register.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.databinding.ItemSelectMainCategoryBinding
import com.ddangddangddang.android.model.CategoriesModel

class MainCategoryAdapter(
    private val onItemClickListener: (Long) -> Unit,
) : ListAdapter<CategoriesModel.CategoryModel, MainCategoryViewHolder>(
    mainCategoryDiffUtil,
) {

    fun setCategories(categories: List<CategoriesModel.CategoryModel>) {
        submitList(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val binding = ItemSelectMainCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MainCategoryViewHolder(binding, onItemClickListener)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val mainCategoryDiffUtil = object : DiffUtil.ItemCallback<CategoriesModel.CategoryModel>() {
            override fun areItemsTheSame(
                oldItem: CategoriesModel.CategoryModel,
                newItem: CategoriesModel.CategoryModel,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CategoriesModel.CategoryModel,
                newItem: CategoriesModel.CategoryModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
