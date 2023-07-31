package com.ddangddangddang.android.feature.register.region

import androidx.lifecycle.ViewModel

class SelectRegionsViewModel() : ViewModel() {

    fun setExitEvent() {

    }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
    }
}
