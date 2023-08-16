package com.ddangddangddang.android.feature.register.category

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ItemSelectMainCategoryBinding
import com.ddangddangddang.android.model.CategoryModel

class MainCategoryViewHolder(
    private val binding: ItemSelectMainCategoryBinding,
    onItemClickListener: (Long) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClickListener
    }

    fun bind(category: CategoryModel) {
        binding.category = category
        if (category.isChecked) {
            binding.clMainCategoryItem.isSelected = true
            binding.tvCategory.setTextColor(binding.root.context.getColor(R.color.white))
            binding.tvCategory.setTypeface(null, Typeface.BOLD)
        } else {
            binding.clMainCategoryItem.isSelected = false
            binding.tvCategory.setTextColor(binding.root.context.getColor(R.color.black_600))
            binding.tvCategory.setTypeface(null, Typeface.NORMAL)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): MainCategoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSelectMainCategoryBinding.inflate(layoutInflater, parent, false)
            return MainCategoryViewHolder(binding, onItemClick)
        }
    }
}