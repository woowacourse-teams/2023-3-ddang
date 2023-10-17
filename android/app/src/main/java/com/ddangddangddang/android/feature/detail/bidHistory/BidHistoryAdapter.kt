package com.ddangddangddang.android.feature.detail.bidHistory

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.di.DefaultDateTimeFormatter
import com.ddangddangddang.android.model.BidHistoryModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ActivityRetainedScoped
class BidHistoryAdapter @Inject constructor(
    @DefaultDateTimeFormatter private val dateTimeFormatter: DateTimeFormatter,
) : ListAdapter<BidHistoryModel, BidHistoryViewHolder>(BidHistoryDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidHistoryViewHolder {
        return BidHistoryViewHolder.create(parent, dateTimeFormatter)
    }

    override fun onBindViewHolder(holder: BidHistoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setBidHistories(histories: List<BidHistoryModel>) {
        submitList(histories)
    }

    companion object {
        private val BidHistoryDiffUtil = object : DiffUtil.ItemCallback<BidHistoryModel>() {
            override fun areItemsTheSame(
                oldItem: BidHistoryModel,
                newItem: BidHistoryModel,
            ): Boolean {
                return oldItem.price == newItem.price
            }

            override fun areContentsTheSame(
                oldItem: BidHistoryModel,
                newItem: BidHistoryModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
