package com.ddangddangddang.android.feature.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemMessageRoomBinding
import com.ddangddangddang.android.model.MessageRoomModel

class MessageRoomViewHolder private constructor(
    private val biding: ItemMessageRoomBinding,
    onClickListener: (MessageRoomModel) -> Unit,
) : RecyclerView.ViewHolder(biding.root) {
    init {
        biding.onClickListener = onClickListener
    }

    fun bind(messageRoomModel: MessageRoomModel) {
        biding.item = messageRoomModel
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClickListener: (MessageRoomModel) -> Unit,
        ): MessageRoomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMessageRoomBinding.inflate(layoutInflater, parent, false)
            return MessageRoomViewHolder(binding, onClickListener)
        }
    }
}
