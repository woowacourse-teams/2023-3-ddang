package com.ddangddangddang.android.feature.register.region

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.RegionSelectionModel

class ThirdRegionsAdapter(private val onItemClickListener: (Long) -> Unit) :
    ListAdapter<RegionSelectionModel, ThirdRegionViewHolder>(RegionsDiffUtil()) {
    fun setRegions(regions: List<RegionSelectionModel>) {
        submitList(regions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThirdRegionViewHolder {
        return ThirdRegionViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ThirdRegionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
