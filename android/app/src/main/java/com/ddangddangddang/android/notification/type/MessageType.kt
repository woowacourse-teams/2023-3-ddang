package com.ddangddangddang.android.notification.type

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ddangddangddang.android.R
import com.ddangddangddang.android.feature.main.MainActivity
import com.ddangddangddang.android.feature.main.MainFragmentType
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.ddangddangddang.android.global.DdangDdangDdang
import com.ddangddangddang.android.global.getBitmapFromUrl
import com.ddangddangddang.android.notification.CHANNEL_ID
import com.ddangddangddang.android.notification.getActiveNotification
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking

object MessageType : NotificationType() {
    private val defaultImage: Bitmap by lazy {
        BitmapFactory.decodeResource(
            DdangDdangDdang.resources,
            R.drawable.img_default_profile,
        )
    }
    override val tag: String = this::class.java.simpleName

    override fun createNotification(
        context: Context,
        id: Long,
        remoteMessage: RemoteMessage,
    ): Notification {
        return runBlocking {
            val image = runCatching {
                getBitmapFromUrl(context, remoteMessage.data["image"] ?: "")
            }.getOrDefault(defaultImage)
            val activeNotification = context.getActiveNotification(tag, id.toInt())
            val currentLine = remoteMessage.data["body"] ?: ""
            val pendingIntent =
                activeNotification?.contentIntent ?: getMessageRoomPendingIntent(context, id)

            Notification.Builder(context.applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.img_logo)
                setLargeIcon(image)
                setShowWhen(true)
                setContentTitle(remoteMessage.data["title"])
                setContentText(currentLine)
                style = getMessageInboxStyle(activeNotification, currentLine)
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }.build()
        }
    }

    private fun getMessageInboxStyle(
        activeNotification: Notification?,
        currentLine: String,
    ): Notification.InboxStyle {
        val previousLines =
            activeNotification?.extras?.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                ?: emptyArray()
        val lines = previousLines.plus(currentLine)

        return Notification.InboxStyle().apply {
            lines.forEach { addLine(it) }
            setSummaryText("${lines.size}개의 메시지")
        }
    }

    private fun getMessageRoomPendingIntent(context: Context, id: Long): PendingIntent? {
        val parentIntent = MainActivity.getIntent(context, MainFragmentType.MESSAGE)
        val intent = MessageRoomActivity.getIntent(context.applicationContext, id)
        return intent.getPendingIntent(context, id.toInt(), parentIntent)
    }
}
