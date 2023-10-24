package com.ddangddangddang.android.notification

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ddangddangddang.android.global.DdangDdangDdang
import com.ddangddangddang.android.notification.type.MessageType
import com.ddangddangddang.android.notification.type.toNotificationType
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
            val type = remoteMessage.data["type"]?.toNotificationType() ?: return
            val id = remoteMessage.data["redirectUrl"]?.split("/")?.last()?.toLong() ?: -1

            if (type is MessageType) {
                val activeRoomId = (application as DdangDdangDdang).activeMessageRoomId
                sendBroadcastToMessageReceiver(id) // 항상 호출. 이 리시버를 받을지 말지는 거기서 정함.
                if (activeRoomId == id) return
            }

            val notification = type.createNotification(applicationContext, id, remoteMessage)
            notificationManager.notify(type.tag, id.toInt(), notification)
        }
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
        return notificationManager.areNotificationsEnabled()
    }

    private fun sendBroadcastToMessageReceiver(roomId: Long) {
        val intent = MessageReceiver.getIntent(roomId)
        sendBroadcast(intent)
    }
}
