package com.ddangddangddang.android.feature.register.region

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.RegionSelectionModel

class RegionSelectionAdapter(private val onCloseItemClickListener: (Long) -> Unit) :
    ListAdapter<RegionSelectionModel, RegionSelectionViewHolder>(RegionsDiffUtil()) {
    fun setRegions(regions: List<RegionSelectionModel>) {
        submitList(regions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionSelectionViewHolder {
        return RegionSelectionViewHolder.create(parent, onCloseItemClickListener)
    }

    override fun onBindViewHolder(holder: RegionSelectionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
