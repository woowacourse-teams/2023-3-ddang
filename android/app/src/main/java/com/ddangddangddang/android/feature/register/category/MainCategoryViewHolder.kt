package com.ddangddangddang.android.feature.register.category

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
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
            binding.tvCategory.setTextColor(binding.root.context.getColor(R.color.grey_50))
            binding.tvCategory.typeface = Typeface.create(ResourcesCompat.getFont(binding.root.context, R.font.pretendard), 700, false)
        } else {
            binding.clMainCategoryItem.isSelected = false
            binding.tvCategory.setTextColor(binding.root.context.getColor(R.color.grey_700))
            binding.tvCategory.typeface = Typeface.create(ResourcesCompat.getFont(binding.root.context, R.font.pretendard), 400, false)
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
