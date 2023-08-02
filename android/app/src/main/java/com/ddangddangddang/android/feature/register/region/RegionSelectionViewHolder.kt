package com.ddangddangddang.android.feature.register.region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemRegionSelectionBinding
import com.ddangddangddang.android.model.RegionSelectionModel

class RegionSelectionViewHolder(
    private val binding: ItemRegionSelectionBinding,
    onCloseItemClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onCloseItemClick = onCloseItemClick
    }

    fun bind(region: RegionSelectionModel) {
        binding.region = region
    }

    companion object {
        fun create(parent: ViewGroup, onCloseItemClick: (Long) -> Unit): RegionSelectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRegionSelectionBinding.inflate(layoutInflater, parent, false)
            return RegionSelectionViewHolder(binding, onCloseItemClick)
        }
    }
}
