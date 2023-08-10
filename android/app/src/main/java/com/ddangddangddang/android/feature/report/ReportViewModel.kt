package com.ddangddangddang.android.feature.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.AuctionRepository

class ReportViewModel(private val repository: AuctionRepository) : ViewModel() {
    private val _event = SingleLiveEvent<ReportEvent>()
    val event: LiveData<ReportEvent>
        get() = _event

    val reportContents = MutableLiveData<String>()

    fun setExitEvent() {
        _event.value = ReportEvent.ExitEvent
    }

    fun submitEvent() {
        // 통신
        _event.value = ReportEvent.submitEvent
    }

    sealed class ReportEvent {
        object ExitEvent : ReportEvent()
        object submitEvent : ReportEvent()
    }
}
