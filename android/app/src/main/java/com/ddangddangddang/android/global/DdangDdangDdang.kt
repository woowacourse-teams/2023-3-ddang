package com.ddangddangddang.android.global

import android.app.Application
import com.ddangddangddang.android.BuildConfig
import com.ddangddangddang.android.notification.createNotificationChannel
import com.google.firebase.analytics.FirebaseAnalytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DdangDdangDdang : Application() {
    override fun onCreate() {
        super.onCreate()
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        KakaoSdk.init(this, BuildConfig.KEY_KAKAO)

        createNotificationChannel(this)
    }

    companion object {
        private var _firebaseAnalytics: FirebaseAnalytics? = null
        val firebaseAnalytics: FirebaseAnalytics?
            get() = _firebaseAnalytics
    }
}
