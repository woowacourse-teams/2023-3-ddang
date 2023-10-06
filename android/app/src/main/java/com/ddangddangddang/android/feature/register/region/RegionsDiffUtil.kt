package com.ddangddangddang.android.feature.register.region

import androidx.recyclerview.widget.DiffUtil
import com.ddangddangddang.android.model.RegionSelectionModel

class RegionsDiffUtil : DiffUtil.ItemCallback<RegionSelectionModel>() {
    override fun areItemsTheSame(
        oldItem: RegionSelectionModel,
        newItem: RegionSelectionModel,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RegionSelectionModel, newItem: RegionSelectionModel): Boolean {
        return oldItem == newItem
    }
}
