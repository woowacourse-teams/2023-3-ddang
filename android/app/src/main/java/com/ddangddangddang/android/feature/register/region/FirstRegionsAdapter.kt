package com.ddangddangddang.android.feature.register.region

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.RegionSelectionModel

class FirstRegionsAdapter(private val onItemClickListener: (Long) -> Unit) :
    ListAdapter<RegionSelectionModel, FirstRegionViewHolder>(RegionsDiffUtil()) {
    fun setRegions(regions: List<RegionSelectionModel>) {
        submitList(regions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstRegionViewHolder {
        return FirstRegionViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: FirstRegionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
