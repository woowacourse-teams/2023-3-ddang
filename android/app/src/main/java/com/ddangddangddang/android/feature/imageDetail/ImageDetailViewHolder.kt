package com.ddangddangddang.android.feature.imageDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemDetailImageBinding

class ImageDetailViewHolder private constructor(
    private val binding: ItemDetailImageBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUrl: String) {
        binding.imageUrl = imageUrl
    }

    companion object {
        fun create(
            parent: ViewGroup,
        ): ImageDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDetailImageBinding.inflate(layoutInflater, parent, false)
            return ImageDetailViewHolder(binding)
        }
    }
}
