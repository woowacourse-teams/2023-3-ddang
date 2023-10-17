package com.ddangddangddang.android.feature.detail.bidHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemBidHistoryBinding
import com.ddangddangddang.android.model.BidHistoryModel
import java.time.format.DateTimeFormatter

class BidHistoryViewHolder private constructor(
    private val binding: ItemBidHistoryBinding,
    dateTimeFormatter: DateTimeFormatter,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.dateTimeFormatter = dateTimeFormatter
    }

    fun bind(item: BidHistoryModel) {
        binding.item = item
    }

    companion object {
        fun create(
            parent: ViewGroup,
            dateTimeFormatter: DateTimeFormatter,
        ): BidHistoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBidHistoryBinding.inflate(layoutInflater, parent, false)
            return BidHistoryViewHolder(binding, dateTimeFormatter)
        }
    }
}
