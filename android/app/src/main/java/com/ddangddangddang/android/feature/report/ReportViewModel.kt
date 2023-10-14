package com.ddangddangddang.android.feature.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.ReportInfo
import com.ddangddangddang.android.model.ReportType
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.ReportAnswerRequest
import com.ddangddangddang.data.model.request.ReportQuestionRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val repository: AuctionRepository) : ViewModel() {
    private val _event = SingleLiveEvent<ReportEvent>()
    val event: LiveData<ReportEvent>
        get() = _event

    private lateinit var reportType: ReportType
    private lateinit var reportInfo: ReportInfo
    val reportContents = MutableLiveData<String>()

    private var isLoading: Boolean = false

    fun setReportInfo(type: ReportType, info: ReportInfo) {
        reportType = type
        reportInfo = info
    }

    fun setExitEvent() {
        _event.value = ReportEvent.ExitEvent
    }

    private fun setBlankContentsEvent() {
        _event.value = ReportEvent.BlankContentsEvent
    }

    fun submit() {
        when (reportInfo) {
            is ReportInfo.ArticleReportInfo -> reportAuctionArticle((reportInfo as ReportInfo.ArticleReportInfo).auctionId)
            is ReportInfo.MessageRoomReportInfo -> reportMessageRoom((reportInfo as ReportInfo.MessageRoomReportInfo).roomId)
            is ReportInfo.QuestionReportInfo -> reportQuestion(reportInfo as ReportInfo.QuestionReportInfo)
            is ReportInfo.AnswerReportInfo -> reportAnswer(reportInfo as ReportInfo.AnswerReportInfo)
        }
    }

    private fun reportAuctionArticle(id: Long) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            reportContents.value?.let { contents ->
                if (contents.isEmpty()) return@launch setBlankContentsEvent() // 내용이 비어있는 경우
                when (val response = repository.reportAuction(id, contents)) {
                    is ApiResponse.Success -> _event.value = ReportEvent.SubmitEvent // 정상적인 신고 접수
                    is ApiResponse.Failure -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.UNEXPECTED)
                    }
                }
            }
            isLoading = false
        }
    }

    private fun reportMessageRoom(id: Long) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            reportContents.value?.let { contents ->
                if (contents.isEmpty()) return@launch setBlankContentsEvent() // 내용이 비어있는 경우
                when (val response = repository.reportMessageRoom(id, contents)) {
                    is ApiResponse.Success -> _event.value = ReportEvent.SubmitEvent // 정상적인 신고 접수
                    is ApiResponse.Failure -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.UNEXPECTED)
                    }
                }
            }
            isLoading = false
        }
    }

    private fun reportQuestion(reportInfo: ReportInfo.QuestionReportInfo) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            reportContents.value?.let { contents ->
                if (contents.isEmpty()) return@launch setBlankContentsEvent() // 내용이 비어있는 경우
                val request = ReportQuestionRequest(reportInfo.auctionId, reportInfo.questionId, contents)
                when (val response = repository.reportQuestion(request)) {
                    is ApiResponse.Success -> _event.value = ReportEvent.SubmitEvent // 정상적인 신고 접수
                    is ApiResponse.Failure -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.UNEXPECTED)
                    }
                }
            }
            isLoading = false
        }
    }

    private fun reportAnswer(reportInfo: ReportInfo.AnswerReportInfo) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            reportContents.value?.let { contents ->
                if (contents.isEmpty()) return@launch setBlankContentsEvent() // 내용이 비어있는 경우
                val request = ReportAnswerRequest(reportInfo.auctionId, reportInfo.answerId, contents)
                when (val response = repository.reportAnswer(request)) {
                    is ApiResponse.Success -> _event.value = ReportEvent.SubmitEvent // 정상적인 신고 접수
                    is ApiResponse.Failure -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.FAILURE(response.error))
                    }

                    is ApiResponse.NetworkError -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.NETWORK_ERROR)
                    }

                    is ApiResponse.Unexpected -> {
                        _event.value =
                            ReportEvent.ReportFailure(ErrorType.UNEXPECTED)
                    }
                }
            }
            isLoading = false
        }
    }

    sealed class ReportEvent {
        object ExitEvent : ReportEvent()
        object SubmitEvent : ReportEvent()
        object BlankContentsEvent : ReportEvent()
        data class ReportFailure(val error: ErrorType) : ReportEvent()
    }
}
