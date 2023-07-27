package com.ddangddangddang.android.feature.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemRegisterImageBinding

class RegisterAuctionImageViewHolder private constructor(private val binding: ItemRegisterImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUrl: String) {
        binding.imageUrl = imageUrl
    }

    companion object {
        fun create(parent: ViewGroup): RegisterAuctionImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRegisterImageBinding.inflate(layoutInflater, parent, false)
            return RegisterAuctionImageViewHolder(binding)
        }
    }
}
