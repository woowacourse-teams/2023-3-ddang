package com.ddangddangddang.android.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class AuctionDetailViewModel : ViewModel() {
    private val _event: SingleLiveEvent<MainScreenEvent> = SingleLiveEvent()
    val event: LiveData<MainScreenEvent>
        get() = _event

    fun exit() {
        _event.value = MainScreenEvent.Exit
    }

    sealed class MainScreenEvent {
        object Exit : MainScreenEvent()
    }
}
