package com.ddangddangddang.android.feature.message

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.MessageRoomModel

class MessageRoomAdapter(
    private val onItemClickListener: (roomId: Long) -> Unit,
) : ListAdapter<MessageRoomModel, MessageRoomViewHolder>(MessageRoomDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRoomViewHolder {
        return MessageRoomViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MessageRoomViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setMessageRooms(list: List<MessageRoomModel>) {
        submitList(list)
    }

    companion object {
        private val MessageRoomDiffUtil = object : DiffUtil.ItemCallback<MessageRoomModel>() {
            override fun areItemsTheSame(
                oldItem: MessageRoomModel,
                newItem: MessageRoomModel,
            ): Boolean {
                return oldItem.roomId == newItem.roomId
            }

            override fun areContentsTheSame(
                oldItem: MessageRoomModel,
                newItem: MessageRoomModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
