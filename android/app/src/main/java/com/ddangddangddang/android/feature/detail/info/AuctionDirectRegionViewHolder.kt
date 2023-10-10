package com.ddangddangddang.android.feature.detail.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemDetailDirectRegionBinding
import com.ddangddangddang.android.model.RegionModel

class AuctionDirectRegionViewHolder private constructor(
    private val binding: ItemDetailDirectRegionBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(directRegion: RegionModel) {
        binding.region = directRegion
    }

    companion object {
        fun create(parent: ViewGroup): AuctionDirectRegionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDetailDirectRegionBinding.inflate(layoutInflater, parent, false)
            return AuctionDirectRegionViewHolder(binding)
        }
    }
}
