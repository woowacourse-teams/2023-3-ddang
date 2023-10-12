package com.ddangddangddang.android.feature.detail.bidHistrory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemBidHistoryBinding
import com.ddangddangddang.android.model.BidHistoryModel

class BidHistoryViewHolder private constructor(
    private val binding: ItemBidHistoryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BidHistoryModel) {
    }

    companion object {
        fun create(
            parent: ViewGroup,
        ): BidHistoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBidHistoryBinding.inflate(layoutInflater, parent, false)
            return BidHistoryViewHolder(binding)
        }
    }
}
