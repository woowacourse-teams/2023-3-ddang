package com.ddangddangddang.android.feature.detail.qna.writequestion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.AskQuestionRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class WriteQuestionViewModel @Inject constructor(private val repository: AuctionRepository) : ViewModel() {
    private val _event: SingleLiveEvent<WriteQuestionEvent> = SingleLiveEvent()
    val event: LiveData<WriteQuestionEvent>
        get() = _event

    val content = MutableLiveData("")

    private val isLoading = AtomicBoolean(false)
    private var auctionId: Long? = null

    fun initAuctionId(auctionId: Long) {
        this.auctionId = auctionId
    }

    fun cancel() {
        _event.value = WriteQuestionEvent.Cancel
    }

    fun submit() {
        if (isLoading.getAndSet(true) || auctionId == null) return
        viewModelScope.launch {
            auctionId?.let { id ->
                when (val response = repository.askQuestion(AskQuestionRequest(id, content.value ?: ""))) {
                    is ApiResponse.Success ->
                        _event.value = WriteQuestionEvent.SubmitQuestion

                    is ApiResponse.Failure ->
                        _event.value =
                            WriteQuestionEvent.FailureSubmitQuestion(ErrorType.FAILURE(response.error))

                    is ApiResponse.NetworkError ->
                        _event.value =
                            WriteQuestionEvent.FailureSubmitQuestion(ErrorType.NETWORK_ERROR)

                    is ApiResponse.Unexpected ->
                        _event.value =
                            WriteQuestionEvent.FailureSubmitQuestion(ErrorType.UNEXPECTED)
                }
                isLoading.set(false)
            }
        }
    }

    sealed class WriteQuestionEvent {
        object Cancel : WriteQuestionEvent()
        object SubmitQuestion : WriteQuestionEvent()
        data class FailureSubmitQuestion(val errorType: ErrorType) : WriteQuestionEvent()
    }
}
