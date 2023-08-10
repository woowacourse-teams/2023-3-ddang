package com.ddangddangddang.android.feature.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentMessageBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingFragment

class MessageFragment : BindingFragment<FragmentMessageBinding>(R.layout.fragment_message) {
    private val viewModel: MessageViewModel by viewModels { viewModelFactory }
    private val messageRoomAdapter: MessageRoomAdapter = MessageRoomAdapter { roomId ->
        viewModel.navigateToMessageRoom(roomId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupMessageRoomsRecyclerView()
        if (viewModel.messageRooms.value == null) viewModel.loadMessageRooms()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) viewModel.loadMessageRooms()
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
        }
    }

    private fun navigateToMessageRoom(roomId: Long) {
    }
}
