package com.ddangddangddang.android.feature.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.MessageRoomModel
import com.ddangddangddang.android.model.mapper.MessageRoomModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.ChatRepository
import kotlinx.coroutines.launch

class MessageViewModel(
    private val repository: ChatRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<MessageEvent> = SingleLiveEvent()
    val event: LiveData<MessageEvent>
        get() = _event

    private val _messageRooms: MutableLiveData<List<MessageRoomModel>> = MutableLiveData()
    val messageRooms: LiveData<List<MessageRoomModel>>
        get() = _messageRooms

    fun loadMessageRooms() {
        viewModelScope.launch {
            when (val response = repository.getChatRoomPreviews()) {
                is ApiResponse.Success -> {
                    _messageRooms.value = response.body.map { it.toPresentation() }
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun navigateToMessageRoom(roomId: Long) {
        _event.value = MessageEvent.NavigateToMessageRoom(roomId)
    }

    sealed class MessageEvent {
        data class NavigateToMessageRoom(val roomId: Long) : MessageEvent()
    }
}
