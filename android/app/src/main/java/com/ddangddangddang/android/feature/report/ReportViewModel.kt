package com.ddangddangddang.android.feature.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class ReportViewModel : ViewModel() {
    private val _event = SingleLiveEvent<ReportEvent>()
    val event: LiveData<ReportEvent>
        get() = _event

    val reportContents = MutableLiveData<String>()

    fun setExitEvent() {
        _event.value = ReportEvent.ExitEvent
    }

    sealed class ReportEvent() {
        object ExitEvent : ReportEvent()
    }
}
