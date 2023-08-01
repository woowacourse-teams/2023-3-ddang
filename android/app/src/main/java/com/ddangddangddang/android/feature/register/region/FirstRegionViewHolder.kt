package com.ddangddangddang.android.feature.register.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): FirstRegionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSelectRegionFirstBinding.inflate(layoutInflater)
            return FirstRegionViewHolder(binding, onItemClick)
        }
    }
}
