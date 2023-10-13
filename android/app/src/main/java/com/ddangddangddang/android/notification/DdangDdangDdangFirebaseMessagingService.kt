package com.ddangddangddang.android.notification

import android.Manifest
import android.app.Notification
import android.app.Notification.EXTRA_TEXT_LINES
import android.app.Notification.InboxStyle
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import com.bumptech.glide.Glide
import com.ddangddangddang.android.R
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.ddangddangddang.android.global.DdangDdangDdang
import com.ddangddangddang.android.reciever.MessageReceiver
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DdangDdangDdangFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var userRepository: UserRepository

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val defaultImage: Bitmap by lazy {
        BitmapFactory.decodeResource(
            resources,
            R.drawable.img_default_profile,
        )
    }

    override fun onNewToken(token: String) {
        runBlocking {
            withContext(Dispatchers.IO) {
                val deviceTokenRequest = UpdateDeviceTokenRequest(token)
                userRepository.updateDeviceToken(deviceTokenRequest)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            notifyNotification(remoteMessage)
        }
    }

    private fun notifyNotification(remoteMessage: RemoteMessage) {
        if (checkNotificationPermission()) {
            val type = NotificationType.of(remoteMessage.data["type"] ?: "") ?: return
            val tag = type.name
            val id = remoteMessage.data["redirectUrl"]?.split("/")?.last()?.toLong() ?: -1
            when (type) {
                NotificationType.MESSAGE -> {
                    val activeRoomId = (application as DdangDdangDdang).activeMessageRoomId
                    sendBroadcastToMessageReceiver(id) // 항상 호출. 이 리시버를 받을지 말지는 거기서 정함.
                    if (activeRoomId != id) {
                        val notification = createMessageNotification(tag, id, remoteMessage)
                        notificationManager.notify(tag, id.toInt(), notification)
                    }
                }

                NotificationType.BID -> {
                    val notification = createBidNotification(id, remoteMessage)
                    notificationManager.notify(tag, id.toInt(), notification)
                }
            }
        }
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
        return notificationManager.areNotificationsEnabled()
    }

    private fun createMessageNotification(
        tag: String,
        id: Long,
        remoteMessage: RemoteMessage,
    ): Notification {
        return runBlocking {
            val image = runCatching {
                getBitmapFromUrl(remoteMessage.data["image"] ?: "")
            }.getOrDefault(defaultImage)
            val activeNotification = getActiveNotification(tag, id.toInt())
            val currentLine = remoteMessage.data["body"] ?: ""
            val pendingIntent =
                activeNotification?.contentIntent ?: getMessageRoomPendingIntent(id)

            Notification.Builder(applicationContext, CHANNEL_ID).apply {
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

    private suspend fun getBitmapFromUrl(url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            Glide.with(applicationContext)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        }
    }

    private fun getMessageInboxStyle(
        activeNotification: Notification?,
        currentLine: String,
    ): InboxStyle {
        val previousLines =
            activeNotification?.extras?.getCharSequenceArray(EXTRA_TEXT_LINES) ?: emptyArray()
        val lines = previousLines.plus(currentLine)

        return InboxStyle().apply {
            lines.forEach { addLine(it) }
            setSummaryText("${lines.size}개의 메시지")
        }
    }

    private fun getMessageRoomPendingIntent(id: Long): PendingIntent? {
        val intent = MessageRoomActivity.getIntent(applicationContext, id)
        return intent.getPendingIntent(id.toInt())
    }

    private fun Intent.getPendingIntent(requestCode: Int): PendingIntent? {
        return PendingIntent.getActivity(
            applicationContext,
            requestCode,
            this,
            FLAG_IMMUTABLE,
        )
    }

    private fun sendBroadcastToMessageReceiver(roomId: Long) {
        val intent = MessageReceiver.getIntent(roomId)
        sendBroadcast(intent)
    }

    private fun createBidNotification(id: Long, remoteMessage: RemoteMessage): Notification {
        return runBlocking {
            val image = runCatching {
                getBitmapFromUrl(remoteMessage.data["image"] ?: "")
            }.getOrDefault(defaultImage)
            val pendingIntent = getAuctionDetailPendingIntent(id)

            Notification.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.img_logo)
                setLargeIcon(image)
                setContentTitle(remoteMessage.data["title"])
                setContentText(remoteMessage.data["body"])
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }.build()
        }
    }

    private fun getAuctionDetailPendingIntent(id: Long): PendingIntent? {
        val intent = AuctionDetailActivity.getIntent(applicationContext, id)
        return intent.getPendingIntent(id.toInt())
    }
}
