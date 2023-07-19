package com.ddangddangddang.android.feature.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.model.RegionModel

class AuctionDirectRegionsAdapter : RecyclerView.Adapter<AuctionDirectRegionViewHolder>() {
    private var regions: List<RegionModel> = listOf()
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

    fun setRegions(regions: List<RegionModel>) {
        this.regions = regions
    }
}
