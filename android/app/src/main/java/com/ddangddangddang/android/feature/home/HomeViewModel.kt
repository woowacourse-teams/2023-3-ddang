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
    private var _page = DEFAULT_PAGE
    val page: Int
        get() = _page

    private var _isLast = false
    val isLast: Boolean
        get() = _isLast

    private val _event: SingleLiveEvent<HomeEvent> = SingleLiveEvent()
    val event: LiveData<HomeEvent>
        get() = _event

    fun loadAuctions() {
        if (!loadingAuctionInProgress) fetchAuctions(_page + 1)
    }

    fun navigateToAuctionDetail(auctionId: Long) {
        _event.value = HomeEvent.NavigateToAuctionDetail(auctionId)
    }

    fun navigateToRegisterAuction() {
        _event.value = HomeEvent.NavigateToRegisterAuction
    }

    fun reloadAuctions() {
        if (!loadingAuctionInProgress) fetchAuctions(DEFAULT_PAGE)
    }

    private fun fetchAuctions(newPage: Int) {
        viewModelScope.launch {
            _loadingAuctionsInProgress = true
            when (
                val response =
                    repository.getAuctionPreviews(page = newPage, size = SIZE_AUCTION_LOAD, sortType = sortType)
            ) {
                is ApiResponse.Success -> {
                    _isLast = response.body.isLast
                    _page = newPage
                }

                is ApiResponse.Failure -> {
                    _event.value = HomeEvent.FailureLoadAuctions(response.error)
                }
                is ApiResponse.NetworkError -> {
                    _event.value = HomeEvent.FailureLoadAuctions(response.exception.message)
                }
                is ApiResponse.Unexpected -> {
                    _event.value = HomeEvent.FailureLoadAuctions(response.t?.message)
                }
            }
            _loadingAuctionsInProgress = false
        }
    }

    fun changeFilter(type: SortType) {
        sortType = type
        reloadAuctions()
    }

//    private enum class LoadType {
//        LOAD, RELOAD
//    }

    sealed class HomeEvent {
        data class NavigateToAuctionDetail(val auctionId: Long) : HomeEvent()
        object NavigateToRegisterAuction : HomeEvent()

        data class FailureLoadAuctions(val message: String?) : HomeEvent()
    }

    companion object {
        private const val SIZE_AUCTION_LOAD = 10
        private const val DEFAULT_PAGE = 1
    }
}
