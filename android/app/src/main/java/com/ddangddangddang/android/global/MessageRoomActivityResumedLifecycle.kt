package com.ddangddangddang.android.global

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry

internal class MessageRoomActivityResumedLifecycle(
    application: Application,
    private val lifecycleRegistry: LifecycleRegistry,
) : Lifecycle by lifecycleRegistry {

    init {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks())
    }

    private inner class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            if (activity is MessageRoomActivity) {
                lifecycleRegistry.onNext(Lifecycle.State.Started)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity is MessageRoomActivity) {
                lifecycleRegistry.onNext(
                    Lifecycle.State.Stopped.WithReason(
                        ShutdownReason(
                            1000,
                            "MessageRoomActivity is paused",
                        ),
                    ),
                )
            }
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityDestroyed(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    }
}
