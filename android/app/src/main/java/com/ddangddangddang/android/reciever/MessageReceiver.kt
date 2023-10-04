package com.ddangddangddang.android.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageReceiver(private val onReceive: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (MessageAction == intent?.action) {
            onReceive()
        }
    }

    companion object {
        const val MessageAction = "com.ddangddangddang.android.message.receive.action"
    }
}
