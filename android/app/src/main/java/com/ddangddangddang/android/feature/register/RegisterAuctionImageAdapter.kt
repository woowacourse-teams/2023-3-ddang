package com.ddangddangddang.android.feature.register

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.RegisterImageModel

class RegisterAuctionImageAdapter(private val onDeleteImageListener: (RegisterImageModel) -> Unit) :
    ListAdapter<RegisterImageModel, RegisterAuctionImageViewHolder>(RegisterAuctionImageDiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RegisterAuctionImageViewHolder {
        return RegisterAuctionImageViewHolder.create(parent, onDeleteImageListener)
    }

    override fun onBindViewHolder(holder: RegisterAuctionImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setImages(images: List<RegisterImageModel>) {
        submitList(images)
    }

    companion object {
        private val RegisterAuctionImageDiffUtil =
            object : DiffUtil.ItemCallback<RegisterImageModel>() {
                override fun areItemsTheSame(
                    oldItem: RegisterImageModel,
                    newItem: RegisterImageModel,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: RegisterImageModel,
                    newItem: RegisterImageModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
