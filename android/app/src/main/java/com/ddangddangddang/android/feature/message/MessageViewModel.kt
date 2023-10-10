package com.ddangddangddang.android.feature.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.MessageRoomModel
import com.ddangddangddang.android.model.mapper.MessageRoomModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
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

                is ApiResponse.Failure -> {
                    _event.value =
                        MessageEvent.MessageLoadFailure(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = MessageEvent.MessageLoadFailure(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = MessageEvent.MessageLoadFailure(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun navigateToMessageRoom(roomId: Long) {
        _event.value = MessageEvent.NavigateToMessageRoom(roomId)
    }

    sealed class MessageEvent {
        data class NavigateToMessageRoom(val roomId: Long) : MessageEvent()
        data class MessageLoadFailure(val error: ErrorType) : MessageEvent()
    }
}
