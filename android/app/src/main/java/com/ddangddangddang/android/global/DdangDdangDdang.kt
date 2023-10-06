package com.ddangddangddang.android.global

import android.app.Application
import android.content.res.Resources
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
        _resources = resources

        KakaoSdk.init(this, BuildConfig.KEY_KAKAO)

        createNotificationChannel(this)
    }

    companion object {
        private lateinit var _firebaseAnalytics: FirebaseAnalytics
        val firebaseAnalytics: FirebaseAnalytics
            get() = _firebaseAnalytics
        private lateinit var _resources: Resources
        val resources: Resources
            get() = _resources
    }
}
