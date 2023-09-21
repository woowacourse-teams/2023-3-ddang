package com.ddangddangddang.android.feature.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.ReportType
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
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
    private var reportId: Long? = null
    val reportContents = MutableLiveData<String>()

    private var isLoading: Boolean = false

    fun setReportInfo(type: ReportType, id: Long) {
        reportType = type
        reportId = id
    }

    fun setExitEvent() {
        _event.value = ReportEvent.ExitEvent
    }

    private fun setBlankContentsEvent() {
        _event.value = ReportEvent.BlankContentsEvent
    }

    fun submit() {
        val reportId: Long = reportId ?: return
        when (reportType) {
            ReportType.ArticleReport -> reportAuctionArticle(reportId)
            ReportType.MessageRoomReport -> reportMessageRoom(reportId)
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
                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
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
                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
                }
            }
            isLoading = false
        }
    }

    sealed class ReportEvent {
        object ExitEvent : ReportEvent()
        object SubmitEvent : ReportEvent()
        object BlankContentsEvent : ReportEvent()
    }
}
