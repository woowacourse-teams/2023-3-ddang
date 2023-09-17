package com.ddangddangddang.android.feature.myAuction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.UserRepository

class MyAuctionViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    sealed class Event {
        object Exit : Event()
    }
}
