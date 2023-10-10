package com.ddangddangddang.android.feature.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AuctionImageAdapter(
    private var images: List<String>,
    private val onImageClick: (image: String) -> Unit = {},
) : RecyclerView.Adapter<AuctionImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionImageViewHolder {
        return AuctionImageViewHolder.create(parent, onImageClick)
    }

    override fun onBindViewHolder(holder: AuctionImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}
