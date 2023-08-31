package com.ddangddangddang.android.global

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

fun screenViewLogEvent(screenName: String) {
    DdangDdangDdang.firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
    }
}
