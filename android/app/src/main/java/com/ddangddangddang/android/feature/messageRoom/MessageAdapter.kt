package com.ddangddangddang.android.feature.messageRoom

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.di.DateFormatter
import com.ddangddangddang.android.di.TimeFormatter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ActivityRetainedScoped
class MessageAdapter @Inject constructor(
    @DateFormatter private val dateFormatter: DateTimeFormatter,
    @TimeFormatter private val timeFormatter: DateTimeFormatter,
) : ListAdapter<MessageViewItem, MessageViewHolder>(MessageDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.of(
            parent,
            MessageViewType.values()[viewType],
            dateFormatter,
            timeFormatter,
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder.MyMessageViewHolder -> holder.bind(currentList[position] as MessageViewItem.MyMessageViewItem)
            is MessageViewHolder.PartnerMessageViewHolder -> holder.bind(currentList[position] as MessageViewItem.PartnerMessageViewItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.ordinal
    }

    fun setMessages(list: List<MessageViewItem>, diffUtilCommitCallback: Runnable? = null) {
        submitList(list, diffUtilCommitCallback)
    }

    companion object {
        private val MessageDiffUtil = object : DiffUtil.ItemCallback<MessageViewItem>() {
            override fun areItemsTheSame(
                oldItem: MessageViewItem,
                newItem: MessageViewItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MessageViewItem,
                newItem: MessageViewItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
