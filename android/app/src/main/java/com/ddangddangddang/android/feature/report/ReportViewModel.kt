package com.ddangddangddang.android.feature.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
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

    fun submit() {
        viewModelScope.launch {
            repository.reportAuction(auctionId ?: return@launch, reportContents.value ?: "")
        }
        setExitEvent()
    }

    sealed class ReportEvent {
        object ExitEvent : ReportEvent()
    }
}
