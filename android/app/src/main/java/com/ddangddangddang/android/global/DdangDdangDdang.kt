package com.ddangddangddang.android.global

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

class DdangDdangDdang : Application() {
    override fun onCreate() {
        super.onCreate()
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    companion object {
        private var _firebaseAnalytics: FirebaseAnalytics? = null
        val firebaseAnalytics: FirebaseAnalytics?
            get() = _firebaseAnalytics
    }
}
