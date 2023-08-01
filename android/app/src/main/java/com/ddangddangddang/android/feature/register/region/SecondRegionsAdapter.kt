package com.ddangddangddang.android.feature.register.region

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.RegionSelectionModel

class SecondRegionsAdapter(private val onItemClickListener: (Long) -> Unit) :
    ListAdapter<RegionSelectionModel, SecondRegionViewHolder>(RegionsDiffUtil()) {
    fun setRegions(regions: List<RegionSelectionModel>) {
        submitList(regions)
//        Log.d("test", currentList.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondRegionViewHolder {
        return SecondRegionViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: SecondRegionViewHolder, position: Int) {
        holder.bind(currentList[position])
        Log.d("test", currentList[position].toString())
    }
}
