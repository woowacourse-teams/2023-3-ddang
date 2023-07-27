package com.ddangddangddang.android.feature.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemRegisterImageBinding

class RegisterAuctionImageViewHolder private constructor(
    private val binding: ItemRegisterImageBinding,
    private val onDeleteImageListener: (String) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.cvImage.setOnClickListener { deleteImage() }
        binding.ivDeleteImage.setOnClickListener { deleteImage() }
    }

    private fun deleteImage() {
        binding.imageUrl?.let { onDeleteImageListener(it) }
    }

    fun bind(imageUrl: String) {
        binding.imageUrl = imageUrl
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteImageListener: (String) -> Unit,
        ): RegisterAuctionImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRegisterImageBinding.inflate(layoutInflater, parent, false)
            return RegisterAuctionImageViewHolder(binding, onDeleteImageListener)
        }
    }
}
