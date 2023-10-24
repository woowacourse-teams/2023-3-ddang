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

    private val _currentPageType = MutableLiveData(OnBoardingPageType.ProfileSetting)
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
            if (it.ordinal == OnBoardingPageType.values().size - 1) return done()
            _currentPageType.value = OnBoardingPageType.values()[it.ordinal + 1]
        }
    }

    private fun done() {
        _event.value = Event.Exit
    }

    fun setSkipEvent() {
        _event.value = Event.Skip
    }

    sealed class Event {
        object Skip : Event()
        object Exit : Event()
    }
}
