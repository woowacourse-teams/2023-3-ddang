package com.ddangddangddang.android.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemHomeAuctionBinding
import com.ddangddangddang.android.model.AuctionHomeModel

class AuctionViewHolder private constructor(private val binding: ItemHomeAuctionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(auction: AuctionHomeModel) {
        binding.auction = auction
    }

    companion object {
        fun create(parent: ViewGroup): AuctionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemHomeAuctionBinding.inflate(layoutInflater, parent, false)
            return AuctionViewHolder(binding)
        }
    }
}
