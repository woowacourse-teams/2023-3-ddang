package com.ddangddangddang.android.feature.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private val _currentPageType = MutableLiveData(OnBoardingPageType.ProfileChange)
    val currentPageType: LiveData<OnBoardingPageType>
        get() = _currentPageType

    fun previousPage() {
        _currentPageType.value?.let {
            if (it.ordinal == 0) return
            _currentPageType.value = OnBoardingPageType.values()[it.ordinal - 1]
        }
    }

    fun nextPage() {
        _currentPageType.value?.let {
            if (it.ordinal == OnBoardingPageType.values().size - 1) {
                _event.value = Event.CompleteExit
            }
            _currentPageType.value = OnBoardingPageType.values()[it.ordinal + 1]
        }
    }

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    sealed class Event {
        object Exit : Event()
        object CompleteExit : Event()
    }
}
