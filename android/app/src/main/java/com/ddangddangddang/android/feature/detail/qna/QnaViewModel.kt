package com.ddangddangddang.android.feature.detail.qna

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.QnaModel
import com.ddangddangddang.android.model.ReportInfo
import com.ddangddangddang.android.model.mapper.QnaModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class QnaViewModel @Inject constructor(private val repository: AuctionRepository) : ViewModel() {

    private val _qnas = MutableLiveData<List<QnaModel.QuestionAndAnswerModel>>()
    val qnas: LiveData<List<QnaModel.QuestionAndAnswerModel>>
        get() = _qnas

    private val isLoading = AtomicBoolean(false)

    private var isOwner: Boolean = false
    private var auctionId: Long? = null

    private val _event: SingleLiveEvent<QnaEvent> = SingleLiveEvent()
    val event: LiveData<QnaEvent>
        get() = _event

    fun initAuctionInfo(isOwner: Boolean, auctionId: Long) {
        this.isOwner = isOwner
        this.auctionId = auctionId
    }

    fun loadQnas() {
        if (isLoading.getAndSet(true)) return
//        this.auctionId = auctionId
        auctionId?.let { auctionId ->

            viewModelScope.launch {
                when (val response = repository.getAuctionQnas(auctionId)) {
                    is ApiResponse.Success ->
                        _qnas.value =
                            response.body.toPresentation(isOwner).questionAndAnswers

                    is ApiResponse.Failure ->
                        _event.value =
                            QnaEvent.FailureLoadQnas(ErrorType.FAILURE(response.error))

                    is ApiResponse.NetworkError ->
                        _event.value =
                            QnaEvent.FailureLoadQnas(ErrorType.NETWORK_ERROR)

                    is ApiResponse.Unexpected ->
                        _event.value =
                            QnaEvent.FailureLoadQnas(ErrorType.UNEXPECTED)
                }
                isLoading.set(false)
            }
        }
    }

    fun selectQna(questionId: Long) {
        _qnas.value?.let {
            _qnas.value = it.map { model ->
                if (model.isPicked && model.question.id == questionId) {
                    model.copy(isPicked = false)
                } else {
                    model.copy(isPicked = model.question.id == questionId)
                }
            }
        }
    }

    fun deleteQuestion(questionId: Long) {
        if (isLoading.getAndSet(true)) return
        viewModelScope.launch {
            val response = repository.deleteQuestion(questionId)
            isLoading.set(false)
            when (response) {
                is ApiResponse.Success -> {
                    auctionId?.let { auctionId ->
                        loadQnas()
                    }
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        QnaEvent.FailureDeleteQuestion(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value =
                        QnaEvent.FailureDeleteQuestion(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value =
                        QnaEvent.FailureDeleteQuestion(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun deleteAnswer(answerId: Long) {
        if (isLoading.getAndSet(true)) return
        viewModelScope.launch {
            val response = repository.deleteAnswer(answerId)
            isLoading.set(false)
            when (response) {
                is ApiResponse.Success -> {
                    auctionId?.let { auctionId ->
                        loadQnas()
                    }
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        QnaEvent.FailureDeleteAnswer(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value =
                        QnaEvent.FailureDeleteAnswer(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value =
                        QnaEvent.FailureDeleteAnswer(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun reportQuestion(questionId: Long) {
        auctionId?.let { auctionId ->
            _event.value =
                QnaEvent.ReportQuestion(ReportInfo.QuestionReportInfo(auctionId, questionId))
        }
    }

    fun reportAnswer(questionId: Long, answerId: Long) {
        auctionId?.let { auctionId ->
            _event.value =
                QnaEvent.ReportAnswer(ReportInfo.AnswerReportInfo(auctionId, questionId, answerId))
        }
    }

    sealed class QnaEvent {
        data class FailureLoadQnas(val errorType: ErrorType) : QnaEvent()
        data class FailureDeleteQuestion(val errorType: ErrorType) : QnaEvent()
        data class FailureDeleteAnswer(val errorType: ErrorType) : QnaEvent()
        data class ReportQuestion(val info: ReportInfo.QuestionReportInfo) : QnaEvent()
        data class ReportAnswer(val info: ReportInfo.AnswerReportInfo) : QnaEvent()
    }
}
