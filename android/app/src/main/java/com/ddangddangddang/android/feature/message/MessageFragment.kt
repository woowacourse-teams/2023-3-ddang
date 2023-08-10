package com.ddangddangddang.android.feature.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentMessageBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingFragment

class MessageFragment : BindingFragment<FragmentMessageBinding>(R.layout.fragment_message) {
    private val viewModel: MessageViewModel by viewModels { viewModelFactory }
    private val adapter: MessageRoomAdapter = MessageRoomAdapter { messageRoomModel ->
        viewModel.navigateToMessageRoom(messageRoomModel.roomId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        if (viewModel.messageRooms.value == null) {
            viewModel.loadMessageRooms()
        }
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
        viewModel.messageRooms.observe(viewLifecycleOwner) { adapter.setMessageRooms(it) }
    }

    private fun handleEvent(event: MessageViewModel.MessageEvent) {
        when (event) {
            is MessageViewModel.MessageEvent.NavigateToMessageRoom -> navigateToMessageRoom(event.roomId)
        }
    }

    private fun navigateToMessageRoom(roomId: Long) {
    }
}
