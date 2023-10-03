package com.ddangddangddang.android.feature.messageRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemMyMessageBinding
import com.ddangddangddang.android.databinding.ItemPartnerMessageBinding
import java.time.format.DateTimeFormatter

sealed class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class MyMessageViewHolder(
        private val binding: ItemMyMessageBinding,
        dateFormatter: DateTimeFormatter,
        timeFormatter: DateTimeFormatter,
    ) : MessageViewHolder(binding.root) {
        init {
            binding.dateFormatter = dateFormatter
            binding.timeFormatter = timeFormatter
        }

        fun bind(item: MessageViewItem.MyMessageViewItem) {
            binding.item = item
        }
    }

    class PartnerMessageViewHolder(
        private val binding: ItemPartnerMessageBinding,
        dateFormatter: DateTimeFormatter,
        timeFormatter: DateTimeFormatter,
    ) : MessageViewHolder(binding.root) {
        init {
            binding.dateFormatter = dateFormatter
            binding.timeFormatter = timeFormatter
        }

        fun bind(item: MessageViewItem.PartnerMessageViewItem) {
            binding.item = item
        }
    }

    companion object {
        fun of(
            parent: ViewGroup,
            type: MessageViewType,
            dateFormatter: DateTimeFormatter,
            timeFormatter: DateTimeFormatter,
        ): MessageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(type.id, parent, false)
            return when (type) {
                MessageViewType.MY_MESSAGE -> MyMessageViewHolder(
                    ItemMyMessageBinding.bind(view),
                    dateFormatter,
                    timeFormatter,
                )

                MessageViewType.PARTNER_MESSAGE -> PartnerMessageViewHolder(
                    ItemPartnerMessageBinding.bind(view),
                    dateFormatter,
                    timeFormatter,
                )
            }
        }
    }
}
