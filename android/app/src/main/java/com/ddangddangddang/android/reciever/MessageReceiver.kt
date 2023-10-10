package com.ddangddangddang.android.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class MessageReceiver(private val onReceive: (messageRoomId: Long) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (MessageAction == intent?.action) {
            val messageRoomId = intent.getLongExtra(MessageRoomId, -1L)
            onReceive(messageRoomId)
        }
    }

    companion object {
        private const val MessageAction = "com.ddangddangddang.android.message.receive.action"
        private const val MessageRoomId = "message_room_id"

        fun getIntent(messageRoomId: Long): Intent {
            return Intent(MessageAction).apply { putExtra(MessageRoomId, messageRoomId) }
        }

        fun getIntentFilter(): IntentFilter {
            return IntentFilter().apply { addAction(MessageAction) }
        }
    }
}
