package com.ddangddangddang.android.feature.detail.bidHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.BidHistoryModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class BidHistoryViewModel @Inject constructor() : ViewModel() {
    private val _histories = MutableLiveData<List<BidHistoryModel>>(listOf())
    val histories: LiveData<List<BidHistoryModel>>
        get() = _histories

    private val isLoading = AtomicBoolean(false)

    fun loadBidHistory() {
        if (isLoading.getAndSet(true)) return
        viewModelScope.launch {
            isLoading.set(false)
        }
    }
}
