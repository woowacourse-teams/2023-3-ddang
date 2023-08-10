package com.ddangddangddang.android.feature.messageRoom

import android.os.Bundle
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMessageRoomBinding
import com.ddangddangddang.android.global.AnalyticsDelegate
import com.ddangddangddang.android.global.AnalyticsDelegateImpl
import com.ddangddangddang.android.util.binding.BindingActivity

class MessageRoomActivity :
    BindingActivity<ActivityMessageRoomBinding>(R.layout.activity_message_room),
    AnalyticsDelegate by AnalyticsDelegateImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAnalytics(javaClass.simpleName, lifecycle)
    }
}
