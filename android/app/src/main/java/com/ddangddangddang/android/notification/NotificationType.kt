package com.ddangddangddang.android.notification

enum class NotificationType(private val value: String) {
    MESSAGE("message"), BID("bid");

    companion object {
        fun of(value: String): NotificationType? {
            return values().find { it.value == value }
        }
    }
}
