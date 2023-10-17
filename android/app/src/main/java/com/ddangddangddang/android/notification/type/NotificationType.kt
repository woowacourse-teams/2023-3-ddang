package com.ddangddangddang.android.notification.type

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.main.MainActivity
import com.google.firebase.messaging.RemoteMessage

internal fun String.toNotificationType(): NotificationType {
    return when (this) {
        "message" -> MessageType
        "bid" -> BidType
        "question" -> QuestionType
        "answer" -> AnswerType
        else -> throw IllegalArgumentException("존재하지 않는 알림 타입 입니다.")
    }
}

abstract class NotificationType {
    abstract val tag: String
    abstract fun createNotification(
        context: Context,
        id: Long,
        remoteMessage: RemoteMessage,
    ): Notification

    fun Intent.getPendingIntent(
        context: Context,
        requestCode: Int,
        parentIntent: Intent? = null,
    ): PendingIntent? {
        return TaskStackBuilder.create(context).run {
            if (parentIntent != null) {
                addNextIntentWithParentStack(parentIntent)
                addNextIntent(this@getPendingIntent)
            } else {
                addNextIntentWithParentStack(this@getPendingIntent)
            }

            getPendingIntent(
                requestCode,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }
}

abstract class AuctionDetailType : NotificationType() {
    abstract override fun createNotification(
        context: Context,
        id: Long,
        remoteMessage: RemoteMessage,
    ): Notification

    fun getAuctionDetailPendingIntent(context: Context, id: Long): PendingIntent? {
        val parentIntent = MainActivity.getIntent(context)
        val intent = AuctionDetailActivity.getIntent(context.applicationContext, id)
        return intent.getPendingIntent(context, id.toInt(), parentIntent)
    }
}
