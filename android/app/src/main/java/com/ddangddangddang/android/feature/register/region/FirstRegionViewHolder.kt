package com.ddangddangddang.android.feature.register.region

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ItemSelectRegionFirstBinding
import com.ddangddangddang.android.model.RegionSelectionModel

class FirstRegionViewHolder(
    private val binding: ItemSelectRegionFirstBinding,
    onItemClickListener: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClickListener
    }

    fun bind(region: RegionSelectionModel) {
        binding.region = region
        if (region.isChecked) {
            binding.clFirstRegionItem.isSelected = true
            binding.tvRegion.setTextColor(binding.root.context.getColor(R.color.grey_50))
            binding.tvRegion.setTypeface(null, Typeface.BOLD)
        } else {
            binding.clFirstRegionItem.isSelected = false
            binding.tvRegion.setTextColor(binding.root.context.getColor(R.color.grey_700))
            binding.tvRegion.setTypeface(null, Typeface.NORMAL)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): FirstRegionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSelectRegionFirstBinding.inflate(layoutInflater, parent, false)
            return FirstRegionViewHolder(binding, onItemClick)
        }
    }
}
