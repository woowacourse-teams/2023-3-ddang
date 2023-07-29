package com.ddangddangddang.android.feature.register.category

import androidx.recyclerview.widget.DiffUtil
import com.ddangddangddang.android.model.CategoriesModel

class CategoryDiffUtil : DiffUtil.ItemCallback<CategoriesModel.CategoryModel>() {
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
