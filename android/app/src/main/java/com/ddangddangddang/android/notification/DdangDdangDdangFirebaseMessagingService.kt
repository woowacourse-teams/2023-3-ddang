package com.ddangddangddang.android.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ddangddangddang.android.R
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class DdangDdangDdangFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var userRepository: UserRepository

    private val notificationManager by lazy { NotificationManagerCompat.from(applicationContext) }

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
            if (checkNotificationPermission()) {
                val notification = createMessageReceivedNotification(remoteMessage)
                notificationManager.notify(System.currentTimeMillis().toInt(), notification)
            }
        }
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
        return notificationManager.areNotificationsEnabled()
    }

    private fun createMessageReceivedNotification(remoteMessage: RemoteMessage): Notification {
        return runBlocking {
            val image =
                runCatching {
                    getBitmapFromUrl(remoteMessage.data["image"] ?: "")
                }.getOrDefault(defaultImage)
            val requestCode = System.currentTimeMillis().toInt()
            val roomId = remoteMessage.data["redirectUrl"]?.split("/")?.last()?.toLong() ?: -1
            val intent = MessageRoomActivity.getIntent(applicationContext, roomId)
            val pendingIntent =
                PendingIntent.getActivity(
                    applicationContext,
                    requestCode,
                    intent,
                    FLAG_IMMUTABLE,
                )

            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.img_logo)
                setLargeIcon(image)
                setContentTitle(remoteMessage.data["title"])
                setContentText(remoteMessage.data["body"])
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }.build()
        }
    }

    private suspend fun getBitmapFromUrl(url: String): Bitmap {
        if (url.isBlank()) throw IllegalArgumentException("url is blank")
        return withContext(Dispatchers.IO) {
            val connection = URL(url).openConnection()
            connection.connect()
            val input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        }
    }
}