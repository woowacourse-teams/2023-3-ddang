package com.ddangddangddang.android.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.SortType
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AuctionRepository) : ViewModel() {
    val auctions: LiveData<List<AuctionHomeModel>> =
        repository.observeAuctionPreviews().map { auctionPreviews ->
            auctionPreviews.map { it.toPresentation() }
        }

    private var _loadingAuctionsInProgress: Boolean = false
    val loadingAuctionInProgress: Boolean
        get() = _loadingAuctionsInProgress

    private var sortType: SortType = SortType.NEW

    private var _page = 0
    val page: Int
        get() = _page

    private var _isLast = false
    val isLast: Boolean
        get() = _isLast

    private val _event: SingleLiveEvent<HomeEvent> = SingleLiveEvent()
    val event: LiveData<HomeEvent>
        get() = _event

    fun loadAuctions() {
        _loadingAuctionsInProgress = true
        viewModelScope.launch {
            _page++
            when (
                val response =
                    repository.getAuctionPreviews(page = _page, size = SIZE_AUCTION_LOAD, sortType = sortType)
            ) {
                is ApiResponse.Success -> {
                    _isLast = response.body.isLast
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
            _loadingAuctionsInProgress = false
        }
    }

    fun navigateToAuctionDetail(auctionId: Long) {
        _event.value = HomeEvent.NavigateToAuctionDetail(auctionId)
    }

    fun navigateToRegisterAuction() {
        _event.value = HomeEvent.NavigateToRegisterAuction
    }

    fun reloadAuctions() {
        if (loadingAuctionInProgress.not()) {
            _loadingAuctionsInProgress = true
            viewModelScope.launch {
                _page = 1
                when (
                    val response =
                        repository.getAuctionPreviews(page = _page, size = SIZE_AUCTION_LOAD, sortType = sortType)
                ) {
                    is ApiResponse.Success -> {
                        _isLast = response.body.isLast
                    }

                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
                }
                _loadingAuctionsInProgress = false
            }
        }
    }

    fun changeFilter(type: SortType) {
        sortType = type
        reloadAuctions()
    }

    sealed class HomeEvent {
        data class NavigateToAuctionDetail(val auctionId: Long) : HomeEvent()
        object NavigateToRegisterAuction : HomeEvent()
    }

    companion object {
        private const val SIZE_AUCTION_LOAD = 10
    }
}
