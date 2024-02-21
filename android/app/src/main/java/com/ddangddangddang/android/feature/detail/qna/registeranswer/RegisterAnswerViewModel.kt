package com.ddangddangddang.android.feature.detail.qna.registeranswer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.RegisterAnswerRequest
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class RegisterAnswerViewModel @Inject constructor(private val repository: AuctionRepository) :
    ViewModel() {

    private val _event: SingleLiveEvent<WriteAnswerEvent> = SingleLiveEvent()
    val event: LiveData<WriteAnswerEvent>
        get() = _event

    val content = MutableLiveData("")

    private val isLoading = AtomicBoolean(false)
    private var auctionId: Long = -1L
    private var questionId: Long = -1L

    fun initIds(auctionId: Long, questionId: Long) {
        this.auctionId = auctionId
        this.questionId = questionId
    }

    fun cancel() {
        _event.value = WriteAnswerEvent.Cancel
    }

    fun submit() {
        if (auctionId == -1L || questionId == -1L) return
        if (isLoading.getAndSet(true)) return
        viewModelScope.launch {
            val request = RegisterAnswerRequest(auctionId, content.value ?: "")
            when (val response = repository.registerAnswer(questionId, request)) {
                is ApiResponse.Success -> _event.value = WriteAnswerEvent.SubmitAnswer
                is ApiResponse.Failure -> {
                    _event.value =
                        WriteAnswerEvent.FailureSubmitAnswer(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value =
                        WriteAnswerEvent.FailureSubmitAnswer(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value =
                        WriteAnswerEvent.FailureSubmitAnswer(ErrorType.UNEXPECTED)
                }
            }
            isLoading.set(false)
        }
    }

    sealed class WriteAnswerEvent {
        object Cancel : WriteAnswerEvent()
        object SubmitAnswer : WriteAnswerEvent()
        data class FailureSubmitAnswer(val errorType: ErrorType) : WriteAnswerEvent()
    }
}
