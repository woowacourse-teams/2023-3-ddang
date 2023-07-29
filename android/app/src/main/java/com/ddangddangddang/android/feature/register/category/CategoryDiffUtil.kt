package com.ddangddangddang.android.feature.register.category

import androidx.recyclerview.widget.DiffUtil
import com.ddangddangddang.android.model.CategoryModel

class CategoryDiffUtil : DiffUtil.ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(
        oldItem: CategoryModel,
        newItem: CategoryModel,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryModel,
        newItem: CategoryModel,
    ): Boolean {
        return oldItem == newItem
    }
}
