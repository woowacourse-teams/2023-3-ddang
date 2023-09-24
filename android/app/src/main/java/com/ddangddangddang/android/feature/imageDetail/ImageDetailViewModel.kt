package com.ddangddangddang.android.feature.imageDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor() : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: SingleLiveEvent<Event>
        get() = _event

    private val _images: MutableLiveData<List<String>> = MutableLiveData()
    val images: LiveData<List<String>>
        get() = _images

    private val _focusPosition: SingleLiveEvent<Int> = SingleLiveEvent()
    val focusPosition: LiveData<Int>
        get() = _focusPosition

    fun setImages(images: List<String>, focusPosition: Int) {
        _images.value = images
        _focusPosition.value = focusPosition
    }

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    sealed class Event {
        object Exit : Event()
    }
}
