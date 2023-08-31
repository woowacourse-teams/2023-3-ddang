package com.ddangddangddang.android.feature.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemRegisterImageBinding
import com.ddangddangddang.android.model.RegisterImageModel

class RegisterAuctionImageViewHolder private constructor(
    private val binding: ItemRegisterImageBinding,
    private val onDeleteImageListener: (RegisterImageModel) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.cvImage.setOnClickListener { deleteImage() }
        binding.ivDeleteImage.setOnClickListener { deleteImage() }
    }

    private fun deleteImage() {
        binding.image?.let { onDeleteImageListener(it) }
    }

    fun bind(image: RegisterImageModel) {
        binding.image = image
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteImageListener: (RegisterImageModel) -> Unit,
        ): RegisterAuctionImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRegisterImageBinding.inflate(layoutInflater, parent, false)
            return RegisterAuctionImageViewHolder(binding, onDeleteImageListener)
        }
    }
}
