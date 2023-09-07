package com.ddangddangddang.android.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import com.ddangddangddang.android.R

private const val CNANNEL_ID = "DDANGDDANGDDANG_CHANNEL_ID"

fun createNotificationChannel(context: Context) {
    val name = context.getString(R.string.alarm_channel_name)
    val descriptionText = context.getString(R.string.alarm_channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val mChannel = NotificationChannel(CNANNEL_ID, name, importance)
    mChannel.description = descriptionText
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(mChannel)
}
