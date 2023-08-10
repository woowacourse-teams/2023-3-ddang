package com.ddangddangddang.android.feature.messageRoom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMessageRoomBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.global.AnalyticsDelegate
import com.ddangddangddang.android.global.AnalyticsDelegateImpl
import com.ddangddangddang.android.util.binding.BindingActivity

class MessageRoomActivity :
    BindingActivity<ActivityMessageRoomBinding>(R.layout.activity_message_room),
    AnalyticsDelegate by AnalyticsDelegateImpl() {
    private val viewModel: MessageRoomViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAnalytics(javaClass.simpleName, lifecycle)
        val roomId: Long = intent.getLongExtra(ROOM_ID_KEY, -1L)
        if (viewModel.messageRoomInfo.value == null) viewModel.loadMessageRoomInfo(roomId)
        setupViewModel()
    }

    private fun setupViewModel() {}

    companion object {
        private const val ROOM_ID_KEY = "room_id_key"

        fun getIntent(context: Context, roomId: Long): Intent {
            return Intent(context, MessageRoomActivity::class.java).apply {
                putExtra(ROOM_ID_KEY, roomId)
            }
        }
    }
}
