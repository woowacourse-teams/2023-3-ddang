package com.ddangddangddang.android.feature.register.region

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ItemSelectRegionSecondBinding
import com.ddangddangddang.android.model.RegionSelectionModel

class SecondRegionViewHolder(
    private val binding: ItemSelectRegionSecondBinding,
    onItemClickListener: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClickListener
    }

    fun bind(region: RegionSelectionModel) {
        binding.region = region
        if (region.isChecked) {
            binding.clSecondRegionItem.isSelected = true
            binding.tvRegion.setTextColor(binding.root.context.getColor(R.color.selected_second_region_text))
            binding.tvRegion.setTypeface(null, Typeface.BOLD)
        } else {
            binding.clSecondRegionItem.isSelected = false
            binding.tvRegion.setTextColor(binding.root.context.getColor(R.color.grey_700))
            binding.tvRegion.setTypeface(null, Typeface.NORMAL)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): SecondRegionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSelectRegionSecondBinding.inflate(layoutInflater, parent, false)
            return SecondRegionViewHolder(binding, onItemClick)
        }
    }
}
