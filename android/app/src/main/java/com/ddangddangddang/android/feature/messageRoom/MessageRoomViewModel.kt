package com.ddangddangddang.android.feature.messageRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.MessageRoomDetailModel
import com.ddangddangddang.android.model.mapper.MessageRoomDetailModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.ChatRepository
import kotlinx.coroutines.launch

class MessageRoomViewModel(
    private val repository: ChatRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<MessageRoomEvent> = SingleLiveEvent()
    val event: LiveData<MessageRoomEvent>
        get() = _event

    private val _messageRoomInfo: MutableLiveData<MessageRoomDetailModel> = MutableLiveData()
    val messageRoomInfo: LiveData<MessageRoomDetailModel>
        get() = _messageRoomInfo

    private var isMessageLoading: Boolean = false

    fun loadMessageRoomInfo(roomId: Long) {
        viewModelScope.launch {
            when (val response = repository.getChatRoomPreview(roomId)) {
                is ApiResponse.Success -> {
                    _messageRoomInfo.value = response.body.toPresentation()
                    loadMessages()
                }

                is ApiResponse.Failure -> {
                    _event.value = MessageRoomEvent.LoadRoomInfoFailed
                }

                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun loadMessages() {
        _messageRoomInfo.value?.let {
            if (isMessageLoading) return
        }
    }

    fun sendMessage() {
        _messageRoomInfo.value?.let { }
    }

    fun setExitEvent() {
        _event.value = MessageRoomEvent.Exit
    }

    fun setReportEvent() {
        _messageRoomInfo.value?.let { _event.value = MessageRoomEvent.Report(it.roomId) }
    }

    fun setNavigateToAuctionDetailEvent() {
        _messageRoomInfo.value?.let {
            _event.value = MessageRoomEvent.NavigateToAuctionDetail(it.auctionId)
        }
    }

    sealed class MessageRoomEvent {
        object Exit : MessageRoomEvent()
        data class Report(val roomId: Long) : MessageRoomEvent()
        data class NavigateToAuctionDetail(val auctionId: Long) : MessageRoomEvent()
        object LoadRoomInfoFailed : MessageRoomEvent()
    }
}
