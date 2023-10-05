package com.ddangddangddang.android.feature.messageRoom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityMessageRoomBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.messageRoom.review.UserReviewDialog
import com.ddangddangddang.android.feature.report.ReportActivity
import com.ddangddangddang.android.global.AnalyticsDelegate
import com.ddangddangddang.android.global.AnalyticsDelegateImpl
import com.ddangddangddang.android.model.ReportType
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageRoomActivity :
    BindingActivity<ActivityMessageRoomBinding>(R.layout.activity_message_room),
    AnalyticsDelegate by AnalyticsDelegateImpl() {
    private val viewModel: MessageRoomViewModel by viewModels()
    private val roomCreatedNotifyAdapter by lazy { RoomCreatedNotifyAdapter() }

    @Inject
    lateinit var messageAdapter: MessageAdapter

    private val adapter by lazy { ConcatAdapter(roomCreatedNotifyAdapter, messageAdapter) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAnalytics(javaClass.simpleName, lifecycle)
        binding.viewModel = viewModel
        val roomId: Long = intent.getLongExtra(ROOM_ID_KEY, -1L)
        if (viewModel.messageRoomInfo.value == null) viewModel.loadMessageRoom(roomId)
        setupViewModel()
        setupMessageRecyclerView()
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) { handleEvent(it) }
        viewModel.messages.observe(this) {
            messageAdapter.setMessages(it) { scrollToDown() }
        }
    }

    private fun setupMessageRecyclerView() {
        binding.rvMessageList.adapter = adapter
        (binding.rvMessageList.layoutManager as LinearLayoutManager).stackFromEnd = true
    }

    private fun handleEvent(event: MessageRoomViewModel.MessageRoomEvent) {
        when (event) {
            is MessageRoomViewModel.MessageRoomEvent.Exit -> finish()
            is MessageRoomViewModel.MessageRoomEvent.Report -> navigateToReport(event.roomId)
            is MessageRoomViewModel.MessageRoomEvent.Rate -> showUserRate()
            is MessageRoomViewModel.MessageRoomEvent.NavigateToAuctionDetail -> {
                navigateToAuctionDetail(event.auctionId)
            }

            is MessageRoomViewModel.MessageRoomEvent.FailureEvent -> handleFailureEvent(event)
        }
    }

    private fun scrollToDown() {
        viewModel.messages.value?.let { binding.rvMessageList.scrollToPosition(it.size) }
    }

    private fun navigateToReport(roomId: Long) {
        startActivity(ReportActivity.getIntent(this, ReportType.MessageRoomReport.ordinal, roomId))
    }

    private fun showUserRate() {
        UserReviewDialog.show(supportFragmentManager)
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        startActivity(AuctionDetailActivity.getIntent(this, auctionId))
    }

    private fun handleFailureEvent(event: MessageRoomViewModel.MessageRoomEvent.FailureEvent) {
        val defaultMessage = getDefaultFailureMessageByFailureEvent(event)
        val actionMessage = getString(R.string.all_snackbar_default_action)
        val message = when (val errorType = event.type) {
            is ErrorType.FAILURE -> errorType.message
            is ErrorType.NETWORK_ERROR -> getString(errorType.messageId)
            is ErrorType.UNEXPECTED -> getString(errorType.messageId)
        }
        binding.root.showSnackbar(
            message = message ?: defaultMessage,
            actionMessage = actionMessage,
        )
    }

    private fun getDefaultFailureMessageByFailureEvent(event: MessageRoomViewModel.MessageRoomEvent.FailureEvent): String {
        return when (event) {
            is MessageRoomViewModel.MessageRoomEvent.FailureEvent.LoadRoomInfo -> {
                getString(R.string.message_room_load_room_info_failed)
            }

            is MessageRoomViewModel.MessageRoomEvent.FailureEvent.LoadMessages -> {
                getString(R.string.message_room_load_messages_failed)
            }

            is MessageRoomViewModel.MessageRoomEvent.FailureEvent.SendMessage -> {
                getString(R.string.message_room_send_message_failed)
            }
        }
    }

    companion object {
        private const val ROOM_ID_KEY = "room_id_key"

        fun getIntent(context: Context, roomId: Long): Intent {
            return Intent(context, MessageRoomActivity::class.java).apply {
                putExtra(ROOM_ID_KEY, roomId)
            }
        }
    }
}
