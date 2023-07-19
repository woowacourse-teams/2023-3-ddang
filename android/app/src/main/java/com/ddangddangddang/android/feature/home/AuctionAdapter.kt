package com.ddangddangddang.android.feature.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.AuctionHomeModel

class AuctionAdapter : ListAdapter<AuctionHomeModel, AuctionViewHolder>(AuctionDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        return AuctionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setAuctions(list: List<AuctionHomeModel>) {
        submitList(list)
    }

    companion object {
        private val AuctionDiffUtil = object : DiffUtil.ItemCallback<AuctionHomeModel>() {
            override fun areItemsTheSame(
                oldItem: AuctionHomeModel,
                newItem: AuctionHomeModel,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AuctionHomeModel,
                newItem: AuctionHomeModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
