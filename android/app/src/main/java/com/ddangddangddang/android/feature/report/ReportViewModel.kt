package com.ddangddangddang.android.feature.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: AuctionRepository) : ViewModel() {
    private val _event = SingleLiveEvent<ReportEvent>()
    val event: LiveData<ReportEvent>
        get() = _event

    private var auctionId: Long? = null
    val reportContents = MutableLiveData<String>()
    fun setAuctionId(id: Long) {
        auctionId = id
    }

    fun setExitEvent() {
        _event.value = ReportEvent.ExitEvent
    }

    private fun setBlankContentsEvent() {
        _event.value = ReportEvent.BlankContentsEvent
    }

    fun submit() {
        viewModelScope.launch {
            reportContents.value?.let { contents ->
                if (contents.isEmpty()) return@launch setBlankContentsEvent() // 내용이 비어있는 경우
                val response = repository.reportAuction(auctionId ?: return@launch, contents)
                when (response) {
                    is ApiResponse.Success -> _event.value = ReportEvent.SubmitEvent // 정상적인 신고 접수
                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
                }
            }
        }
    }
    sealed class ReportEvent {
        object ExitEvent : ReportEvent()
        object SubmitEvent : ReportEvent()
        object BlankContentsEvent : ReportEvent()
    }
}
