package com.ddangddangddang.android.feature.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentMessageBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.messageRoom.MessageRoomActivity
import com.ddangddangddang.android.reciever.MessageReceiver
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BindingFragment<FragmentMessageBinding>(R.layout.fragment_message) {
    private val viewModel: MessageViewModel by viewModels()
    private val messageRoomAdapter: MessageRoomAdapter = MessageRoomAdapter { roomId ->
        viewModel.navigateToMessageRoom(roomId)
    }

    private val messageReceiver: MessageReceiver by lazy {
        MessageReceiver { viewModel.loadMessageRooms() } // 필터링 없이 모든 수신을 받아서 처리 한다.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupMessageRoomsRecyclerView()
        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(messageReceiver, MessageReceiver.getIntentFilter())
        viewModel.loadMessageRooms() // 홈 키에서 돌아올 때, 메시지 방에서 돌아올 때 갱신 되도록 하기 위해 여기 배치
    }

    override fun onPause() {
        super.onPause()
        unregisterMessageReceiver()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) return unregisterMessageReceiver()
        requireContext().registerReceiver(messageReceiver, MessageReceiver.getIntentFilter())
        viewModel.loadMessageRooms()
    }

    private fun unregisterMessageReceiver() {
        runCatching { requireContext().unregisterReceiver(messageReceiver) }
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
        viewModel.messageRooms.observe(viewLifecycleOwner) { messageRoomAdapter.setMessageRooms(it) }
    }

    private fun setupMessageRoomsRecyclerView() {
        with(binding.rvMessageRooms) {
            adapter = messageRoomAdapter
            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                LinearLayoutManager(requireContext()).orientation,
            )
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun handleEvent(event: MessageViewModel.MessageEvent) {
        when (event) {
            is MessageViewModel.MessageEvent.NavigateToMessageRoom -> navigateToMessageRoom(event.roomId)
            is MessageViewModel.MessageEvent.MessageLoadFailure -> {
                requireActivity().notifyFailureMessage(
                    event.error,
                    R.string.message_rooms_load_failure,
                )
            }
        }
    }

    private fun navigateToMessageRoom(roomId: Long) {
        startActivity(MessageRoomActivity.getIntent(requireContext(), roomId))
    }
}
