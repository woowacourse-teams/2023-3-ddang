package com.ddangddangddang.android.feature.imageDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor() : ViewModel() {
    private val _images: MutableLiveData<List<String>> = MutableLiveData()
    val images: LiveData<List<String>>
        get() = _images

    fun setImages(images: List<String>) {
        _images.value = images
    }
}
