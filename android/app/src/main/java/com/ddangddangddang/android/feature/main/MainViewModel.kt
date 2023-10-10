package com.ddangddangddang.android.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _currentFragmentType: MutableLiveData<FragmentType> =
        MutableLiveData(FragmentType.HOME)
    val currentFragmentType: LiveData<FragmentType>
        get() = _currentFragmentType
    private val _event: SingleLiveEvent<MainEvent> = SingleLiveEvent()
    val event: LiveData<MainEvent>
        get() = _event

    val fragmentChange = { fragmentType: FragmentType ->
        changeCurrentFragmentType(fragmentType)
    }

    private fun changeCurrentFragmentType(fragmentType: FragmentType) {
        if (currentFragmentType.value == fragmentType) {
            if (fragmentType == FragmentType.HOME) {
                _event.value = MainEvent.HomeToTop
            }
        }

        _currentFragmentType.value = fragmentType
    }

    sealed class MainEvent {
        object HomeToTop : MainEvent()
    }
}
