package com.ddangddangddang.android.feature.messageRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemMessageRoomCreatedNotifyBinding

class RoomCreatedNotifyAdapter : RecyclerView.Adapter<RoomCreatedNotifyAdapter.NotifyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        return NotifyViewHolder.create(parent)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {}

    class NotifyViewHolder private constructor(biding: ItemMessageRoomCreatedNotifyBinding) :
        RecyclerView.ViewHolder(biding.root) {

        companion object {
            fun create(parent: ViewGroup): NotifyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemMessageRoomCreatedNotifyBinding.inflate(layoutInflater, parent, false)
                return NotifyViewHolder(binding)
            }
        }
    }
}
