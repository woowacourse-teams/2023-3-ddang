package com.ddangddangddang.android.feature.imageDetail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ImageDetailAdapter(
    private var images: List<String>,
) : RecyclerView.Adapter<ImageDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDetailViewHolder {
        return ImageDetailViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageDetailViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}
