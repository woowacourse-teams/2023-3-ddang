package com.ddangddangddang.android.global

import android.app.Application
import com.ddangddangddang.android.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.kakao.sdk.common.KakaoSdk

class DdangDdangDdang : Application() {
    override fun onCreate() {
        super.onCreate()
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        KakaoSdk.init(this, BuildConfig.KEY_KAKAO)
    }

    companion object {
        private var _firebaseAnalytics: FirebaseAnalytics? = null
        val firebaseAnalytics: FirebaseAnalytics?
            get() = _firebaseAnalytics
    }
}
