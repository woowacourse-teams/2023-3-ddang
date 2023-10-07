package com.ddangddangddang.android.feature.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemDetailAuctionBinding

class AuctionImageViewHolder private constructor(private val binding: ItemDetailAuctionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(imageUrl: String) {
        binding.imageUrl = imageUrl
    }

    companion object {
        fun create(parent: ViewGroup): AuctionImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDetailAuctionBinding.inflate(layoutInflater, parent, false)
            return AuctionImageViewHolder(binding)
        }
    }
}
