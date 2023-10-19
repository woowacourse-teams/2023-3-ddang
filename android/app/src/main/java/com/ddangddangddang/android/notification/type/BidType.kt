package com.ddangddangddang.android.notification.type

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ddangddangddang.android.R
import com.ddangddangddang.android.global.DdangDdangDdang
import com.ddangddangddang.android.global.getBitmapFromUrl
import com.ddangddangddang.android.notification.CHANNEL_ID
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.runBlocking

object BidType : AuctionDetailType() {
    private val defaultImage: Bitmap by lazy {
        BitmapFactory.decodeResource(
            DdangDdangDdang.resources,
            R.drawable.img_default_auction,
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
            val pendingIntent = getAuctionDetailPendingIntent(context, id)

            Notification.Builder(context.applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.img_logo)
                setLargeIcon(image)
                setContentTitle(remoteMessage.data["title"])
                setContentText(remoteMessage.data["body"])
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }.build()
        }
    }
}
