package com.ddangddangddang.android.feature.register.category

import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class SelectCategoryViewModel : ViewModel() {
    private val _event: SingleLiveEvent<SelectCategoryEvent> = SingleLiveEvent()

    fun setExitEvent() {
        _event.value = SelectCategoryEvent.Exit
    }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
    }
}
