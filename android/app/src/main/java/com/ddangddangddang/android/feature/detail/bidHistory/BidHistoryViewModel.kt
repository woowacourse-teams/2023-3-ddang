package com.ddangddangddang.android.feature.detail.bidHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.BidHistoryModel
import com.ddangddangddang.android.model.mapper.AuctionBidHistoryModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class BidHistoryViewModel @Inject constructor(
    val repository: AuctionRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private val _histories = MutableLiveData<List<BidHistoryModel>>(listOf())
    val histories: LiveData<List<BidHistoryModel>>
        get() = _histories

    private val isLoading = AtomicBoolean(false)

    fun loadBidHistory(auctionId: Long) {
        if (isLoading.getAndSet(true)) return
        viewModelScope.launch {
            when (val response = repository.getBidHistories(auctionId)) {
                is ApiResponse.Success -> {
                    _histories.value = response.body.map { it.toPresentation() }.reversed()
                }

                is ApiResponse.Failure -> {
                    _event.value = Event.BidHistoryLoadFailure(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = Event.BidHistoryLoadFailure(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = Event.BidHistoryLoadFailure(ErrorType.UNEXPECTED)
                }
            }
            isLoading.set(false)
        }
    }

    sealed class Event {
        data class BidHistoryLoadFailure(val error: ErrorType) : Event()
    }
}
