package com.ddangddangddang.android.feature.register

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class RegisterAuctionImageAdapter(private val onDeleteImageListener: (String) -> Unit) :
    ListAdapter<String, RegisterAuctionImageViewHolder>(RegisterAuctionImageDiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RegisterAuctionImageViewHolder {
        return RegisterAuctionImageViewHolder.create(parent, onDeleteImageListener)
    }

    override fun onBindViewHolder(holder: RegisterAuctionImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setImages(images: List<String>) {
        submitList(images)
    }

    companion object {
        private val RegisterAuctionImageDiffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
