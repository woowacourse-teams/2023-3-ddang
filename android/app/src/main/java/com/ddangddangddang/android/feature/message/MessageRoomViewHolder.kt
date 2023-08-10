package com.ddangddangddang.android.feature.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemMessageRoomBinding
import com.ddangddangddang.android.model.ChatRoomModel

class MessageRoomViewHolder private constructor(
    private val biding: ItemMessageRoomBinding,
    onClickListener: (ChatRoomModel) -> Unit,
) : RecyclerView.ViewHolder(biding.root) {
    init {
        biding.onClickListener = onClickListener
    }

    fun bind(chatRoomModel: ChatRoomModel) {
        biding.item = chatRoomModel
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClickListener: (ChatRoomModel) -> Unit,
        ): MessageRoomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMessageRoomBinding.inflate(layoutInflater, parent, false)
            return MessageRoomViewHolder(binding, onClickListener)
        }
    }
}
