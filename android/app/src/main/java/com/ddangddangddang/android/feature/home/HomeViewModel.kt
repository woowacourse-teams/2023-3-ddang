package com.ddangddangddang.android.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AuctionRepository) : ViewModel() {
    val auctions: LiveData<List<AuctionHomeModel>> =
        repository.observeAuctionPreviews().map { auctionPreviews ->
            lastAuctionId.value = auctionPreviews.lastOrNull()?.id
            auctionPreviews.map { it.toPresentation() }
        }

    private val lastAuctionId: MutableLiveData<Long?> = MutableLiveData()

    private val _event: SingleLiveEvent<HomeEvent> = SingleLiveEvent()
    val event: LiveData<HomeEvent>
        get() = _event

    fun loadAuctions() {
        if (lastAuctionId.value == null) {
            viewModelScope.launch {
                when (val response = repository.getAuctionPreviews()) {
                    is ApiResponse.Success -> {}
                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
                }
            }
        }
    }

    fun navigateToAuctionDetail(auctionId: Long) {
        _event.value = HomeEvent.NavigateToAuctionDetail(auctionId)
    }

    fun navigateToRegisterAuction() {
        _event.value = HomeEvent.NavigateToRegisterAuction
    }

    sealed class HomeEvent {
        data class NavigateToAuctionDetail(val auctionId: Long) : HomeEvent()
        object NavigateToRegisterAuction : HomeEvent()
    }
}
