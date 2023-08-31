package com.ddangddangddang.android.global

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

interface AnalyticsDelegate {
    val screenName: String
    fun registerAnalytics(screenName: String, lifecycle: Lifecycle)
}

class AnalyticsDelegateImpl :
    AnalyticsDelegate,
    DefaultLifecycleObserver {
    private var _screenName: String = ""
    override val screenName: String
        get() = _screenName

    override fun registerAnalytics(screenName: String, lifecycle: Lifecycle) {
        _screenName = screenName
        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        screenViewLogEvent(screenName)
    }
}
