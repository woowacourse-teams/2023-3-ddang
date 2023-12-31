package com.ddangddangddang.android.feature.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.AuctionHomeModel

class AuctionAdapter(private val onItemClick: (Long) -> Unit) :
    ListAdapter<AuctionHomeModel, AuctionViewHolder>(AuctionDiffUtil) {

    fun setAuctions(list: List<AuctionHomeModel>, callback: (() -> Unit)? = null) {
        submitList(list, callback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        return AuctionViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        holder.bind(currentList[position])
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
