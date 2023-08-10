package com.ddangddangddang.android.feature.messageRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.MessageRoomDetailModel
import com.ddangddangddang.android.model.mapper.MessageRoomDetailModelMapper.toPresentation
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.ChatRepository
import kotlinx.coroutines.launch

class MessageRoomViewModel(
    private val repository: ChatRepository,
) : ViewModel() {
    private val _messageRoomInfo: MutableLiveData<MessageRoomDetailModel> = MutableLiveData()
    val messageRoomInfo: LiveData<MessageRoomDetailModel>
        get() = _messageRoomInfo

    fun loadMessageRoomInfo(roomId: Long) {
        viewModelScope.launch {
            when (val response = repository.getChatRoomPreview(roomId)) {
                is ApiResponse.Success -> {
                    _messageRoomInfo.value = response.body.toPresentation()
                    loadMessages()
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun loadMessages() {
    }
}
