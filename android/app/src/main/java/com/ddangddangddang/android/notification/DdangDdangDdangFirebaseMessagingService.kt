package com.ddangddangddang.android.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class DdangDdangDdangFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // TODO send FCM registration token to your app server.
        Log.d("test", "Refreshed token: $token")
    }
}
