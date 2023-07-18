package com.ddangddangddang.android.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class AuctionDetailViewModel : ViewModel() {
    private val _event: SingleLiveEvent<MainScreenEvent> = SingleLiveEvent()
    val event: LiveData<MainScreenEvent>
        get() = _event

    val images = listOf(
        "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
        "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
        "https://cdn.pixabay.com/photo/2020/03/08/21/41/landscape-4913841_1280.jpg",
        "https://cdn.pixabay.com/photo/2020/09/02/18/03/girl-5539094_1280.jpg",
        "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg",
    )

    fun exit() {
        _event.value = MainScreenEvent.Exit
    }

    sealed class MainScreenEvent {
        object Exit : MainScreenEvent()
    }
}
