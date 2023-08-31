package com.ddangddangddang.android.feature.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemMessageRoomBinding
import com.ddangddangddang.android.model.MessageRoomModel

class MessageRoomViewHolder private constructor(
    private val biding: ItemMessageRoomBinding,
    onItemClickListener: (roomId: Long) -> Unit,
) : RecyclerView.ViewHolder(biding.root) {
    init {
        biding.onItemClickListener = onItemClickListener
    }

    fun bind(messageRoomModel: MessageRoomModel) {
        biding.item = messageRoomModel
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: (roomId: Long) -> Unit,
        ): MessageRoomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMessageRoomBinding.inflate(layoutInflater, parent, false)
            return MessageRoomViewHolder(binding, onItemClickListener)
        }
    }
}
