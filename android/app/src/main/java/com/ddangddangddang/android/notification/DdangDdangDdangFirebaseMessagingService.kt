package com.ddangddangddang.android.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ddangddangddang.android.R
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DdangDdangDdangFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // TODO send FCM registration token to your app server.
        Log.d("test", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Log.d("test", "Message data payload: ${remoteMessage.data}")
                val notification = createMessageReceivedNotification(remoteMessage)
                NotificationManagerCompat.from(applicationContext)
                    .notify(System.currentTimeMillis().toInt(), notification)
            }
        }
    }

    private fun createMessageReceivedNotification(remoteMessage: RemoteMessage): Notification {
        val roomId = remoteMessage.data["redirectUrl"]?.split("/")?.last()?.toLong() ?: -1
        val intent = MessageRoomActivity.getIntent(applicationContext, roomId)
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE,
            )

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.img_logo)
            setContentTitle(remoteMessage.data["title"])
            setContentText(remoteMessage.data["body"])
            setContentIntent(pendingIntent)
        }.build()
    }
}
