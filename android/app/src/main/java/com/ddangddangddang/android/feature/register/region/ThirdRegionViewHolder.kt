package com.ddangddangddang.android.feature.register.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemSelectRegionThirdBinding
import com.ddangddangddang.android.model.RegionSelectionModel

class ThirdRegionViewHolder(
    private val binding: ItemSelectRegionThirdBinding,
    onItemClickListener: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClickListener
    }

    fun bind(region: RegionSelectionModel) {
        binding.region = region
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): ThirdRegionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSelectRegionThirdBinding.inflate(layoutInflater, parent, false)
            return ThirdRegionViewHolder(binding, onItemClick)
        }
    }
}
