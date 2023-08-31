package com.ddangddangddang.android.feature.messageRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemMyMessageBinding
import com.ddangddangddang.android.databinding.ItemPartnerMessageBinding

sealed class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class MyMessageViewHolder(
        private val binding: ItemMyMessageBinding,
    ) : MessageViewHolder(binding.root) {
        fun bind(item: MessageViewItem.MyMessageViewItem) {
            binding.item = item
        }
    }

    class PartnerMessageViewHolder(
        private val binding: ItemPartnerMessageBinding,
    ) : MessageViewHolder(binding.root) {
        fun bind(item: MessageViewItem.PartnerMessageViewItem) {
            binding.item = item
        }
    }

    companion object {
        fun of(
            parent: ViewGroup,
            type: MessageViewType,
        ): MessageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(type.id, parent, false)
            return when (type) {
                MessageViewType.MY_MESSAGE -> MyMessageViewHolder(
                    ItemMyMessageBinding.bind(view),
                )

                MessageViewType.PARTNER_MESSAGE -> PartnerMessageViewHolder(
                    ItemPartnerMessageBinding.bind(view),
                )
            }
        }
    }
}
