package com.ddangddangddang.android.feature.detail.info

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.model.RegionModel

class AuctionDirectRegionsAdapter(private val regions: List<RegionModel> = listOf()) :
    RecyclerView.Adapter<AuctionDirectRegionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AuctionDirectRegionViewHolder {
        return AuctionDirectRegionViewHolder.create(parent)
    }

    override fun getItemCount(): Int = regions.size

    override fun onBindViewHolder(holder: AuctionDirectRegionViewHolder, position: Int) {
        holder.bind(regions[position])
    }
}
