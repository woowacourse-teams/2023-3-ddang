package com.ddangddangddang.android.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context

internal fun Context.getActiveNotification(tag: String, id: Int): Notification? {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.activeNotifications.firstOrNull {
        it.tag == tag && it.id == id
    }?.notification
}

internal fun Context.cancelActiveNotification(id: Int) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.activeNotifications.forEach {
        if (it.id == id) cancelActiveNotification(it.tag, it.id)
    }
}

internal fun Context.cancelActiveNotification(tag: String, id: Int) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(tag, id)
}
