package com.ddangddangddang.android.feature.messageRoom

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class MessageAdapter : ListAdapter<MessageViewItem, MessageViewHolder>(MessageDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.of(parent, MessageViewType.values()[viewType])
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

    fun setMessages(list: List<MessageViewItem>) {
        submitList(list)
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
                return oldItem.id == newItem.id &&
                    oldItem.type == newItem.type &&
                    oldItem.contents == newItem.contents &&
                    oldItem.createdDateTime == newItem.createdDateTime
            }
        }
    }
}
