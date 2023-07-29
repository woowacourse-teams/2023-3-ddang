package com.ddangddangddang.android.feature.register.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ItemSelectMainCategoryBinding
import com.ddangddangddang.android.model.CategoriesModel

class MainCategoryAdapter(
    private val categories: LiveData<List<CategoriesModel.CategoryModel>>,
    private val onItemClickListener: (Long) -> Unit,
) :
    RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder>() {

    class MainCategoryViewHolder(
        private val binding: ItemSelectMainCategoryBinding,
        onItemClickListener: (Long) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.onItemClick = onItemClickListener
        }

        fun bind(category: CategoriesModel.CategoryModel) {
            binding.category = category
            if (category.isChecked) {
                binding.clMainCategoryItem.setBackgroundColor(
                    binding.root.context.getColor(
                        R.color.red_100,
                    ),
                )
            } else {
                binding.clMainCategoryItem.setBackgroundColor(
                    binding.root.context.getColor(
                        R.color.white,
                    ),
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val binding = ItemSelectMainCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MainCategoryViewHolder(binding, onItemClickListener)
    }

    override fun getItemCount(): Int = categories.value?.size ?: 0

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        categories.value?.let {
            holder.bind(it[position])
        }
    }
}
