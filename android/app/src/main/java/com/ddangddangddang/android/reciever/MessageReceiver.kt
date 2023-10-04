package com.ddangddangddang.android.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageReceiver(private val onReceive: (messageRoomId: Long) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (MessageAction == intent?.action) {
            val messageRoomId = intent.getLongExtra(MessageRoomId, -1L)
            onReceive(messageRoomId)
        }
    }

    companion object {
        const val MessageAction = "com.ddangddangddang.android.message.receive.action"
        const val MessageRoomId = "message_room_id"
    }
}
