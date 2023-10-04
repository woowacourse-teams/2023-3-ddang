package com.ddangddangddang.android.feature.messageRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.MessageModel
import com.ddangddangddang.android.model.MessageRoomDetailModel
import com.ddangddangddang.android.model.mapper.MessageModelMapper.toPresentation
import com.ddangddangddang.android.model.mapper.MessageRoomDetailModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.ChatMessageRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MessageRoomViewModel @Inject constructor(
    private val repository: ChatRepository,
) : ViewModel() {
    val inputMessage: MutableLiveData<String> = MutableLiveData("")

    private val _event: SingleLiveEvent<MessageRoomEvent> = SingleLiveEvent()
    val event: LiveData<MessageRoomEvent>
        get() = _event
    private val _messageRoomInfo: MutableLiveData<MessageRoomDetailModel> = MutableLiveData()

    val messageRoomInfo: LiveData<MessageRoomDetailModel>
        get() = _messageRoomInfo
    private val _messages: MutableLiveData<List<MessageViewItem>> = MutableLiveData()
    val messages: LiveData<List<MessageViewItem>>
        get() = _messages

    private val lastMessageId: Long?
        get() = _messages.value?.lastOrNull()?.id

    private var isMessageLoading: Boolean = false

    fun loadMessageRoom(roomId: Long) {
        viewModelScope.launch {
            when (val response = repository.getChatRoomPreview(roomId)) {
                is ApiResponse.Success -> {
                    _messageRoomInfo.value = response.body.toPresentation()
                    loadMessages()
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        MessageRoomEvent.FailureEvent.LoadRoomInfo(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value =
                        MessageRoomEvent.FailureEvent.LoadRoomInfo(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = MessageRoomEvent.FailureEvent.LoadRoomInfo(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun loadMessages() {
        _messageRoomInfo.value?.let {
            if (isMessageLoading) return

            isMessageLoading = true
            viewModelScope.launch {
                when (val response = repository.getMessages(it.roomId, lastMessageId)) {
                    is ApiResponse.Success -> {
                        addMessages(response.body.map { it.toPresentation() }.toViewItems())
                    }

                    is ApiResponse.Failure -> {
                        _event.value =
                            MessageRoomEvent.FailureEvent.LoadMessages(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value =
                            MessageRoomEvent.FailureEvent.LoadMessages(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value =
                            MessageRoomEvent.FailureEvent.LoadMessages(ErrorType.UNEXPECTED)
                    }
                }
                isMessageLoading = false
            }
        }
    }

    private fun addMessages(messages: List<MessageViewItem>) {
        _messages.value = _messages.value?.plus(messages) ?: messages
    }

    private fun List<MessageModel>.toViewItems(): List<MessageViewItem> {
        var previousSendDate = _messages.value?.lastOrNull()?.createdDateTime?.toLocalDate()
        return map { messageModel ->
            val sendDateTime = LocalDateTime.parse(messageModel.createdAt)
            val sendDate = sendDateTime.toLocalDate()
            val isFirstAtDate = (sendDate == previousSendDate).not()
            previousSendDate = sendDate
            messageModel.toViewItem(sendDateTime, isFirstAtDate)
        }
    }

    private fun MessageModel.toViewItem(
        sendDateTime: LocalDateTime,
        isFirstAtDate: Boolean,
    ): MessageViewItem {
        return if (isMyMessage) {
            MessageViewItem.MyMessageViewItem(id, sendDateTime, contents, isFirstAtDate)
        } else {
            MessageViewItem.PartnerMessageViewItem(id, sendDateTime, contents, isFirstAtDate)
        }
    }

    fun sendMessage() {
        _messageRoomInfo.value?.let {
            val message = inputMessage.value
            if (message.isNullOrEmpty()) return

            val request = ChatMessageRequest(it.messagePartnerId, message)
            viewModelScope.launch {
                when (val response = repository.sendMessage(it.roomId, request)) {
                    is ApiResponse.Success -> {
                        inputMessage.value = ""
                        loadMessages()
                    }

                    is ApiResponse.Failure -> {
                        _event.value =
                            MessageRoomEvent.FailureEvent.SendMessage(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value =
                            MessageRoomEvent.FailureEvent.SendMessage(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value =
                            MessageRoomEvent.FailureEvent.SendMessage(ErrorType.UNEXPECTED)
                    }
                }
            }
        }
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
        sealed class FailureEvent(val type: ErrorType) : MessageRoomEvent() {
            class LoadRoomInfo(type: ErrorType) : FailureEvent(type)
            class LoadMessages(type: ErrorType) : FailureEvent(type)
            class SendMessage(type: ErrorType) : FailureEvent(type)
        }
    }
}
