package com.ddangddangddang.android.feature.myAuction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.UserRepository
import kotlinx.coroutines.launch

class MyAuctionViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private val _auctions: MutableLiveData<List<AuctionHomeModel>> = MutableLiveData()
    val auctions: LiveData<List<AuctionHomeModel>>
        get() = _auctions

    private var _loadingAuctionsInProgress: Boolean = false
    val loadingAuctionInProgress: Boolean
        get() = _loadingAuctionsInProgress

    private var _page = 0
    val page: Int
        get() = _page

    private val _isLast = MutableLiveData(false)
    val isLast: LiveData<Boolean>
        get() = _isLast

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    fun navigateToAuctionDetail(auctionId: Long) {
        _event.value = Event.NavigateToAuctionDetail(auctionId)
    }

    fun loadMyAuctions() {
        fetchAuctions(_page + 1)
    }

    fun reloadAuctions() {
        fetchAuctions(DEFAULT_PAGE)
    }

    private fun fetchAuctions(newPage: Int) {
        if (loadingAuctionInProgress) return
        _loadingAuctionsInProgress = true
        viewModelScope.launch {
            when (val response = userRepository.getMyAuctionPreviews(newPage)) {
                is ApiResponse.Success -> {
                    updateAuctions(response.body, newPage)
                }

                is ApiResponse.Failure -> {
                    _event.value = Event.FailureLoadEvent(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = Event.FailureLoadEvent(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = Event.FailureLoadEvent(ErrorType.UNEXPECTED)
                }
            }
            _loadingAuctionsInProgress = false
        }
    }

    private fun updateAuctions(response: AuctionPreviewsResponse, newPage: Int) {
        if (newPage == 1) {
            _auctions.value = response.auctions.map { it.toPresentation() }
        } else {
            val items = _auctions.value ?: emptyList()
            _auctions.value = items + response.auctions.map { it.toPresentation() }
        }

        _isLast.value = response.isLast
        _page = newPage
    }

    sealed class Event {
        object Exit : Event()
        data class NavigateToAuctionDetail(val auctionId: Long) : Event()

        data class FailureLoadEvent(val type: ErrorType) : Event()
    }

    companion object {
        private const val DEFAULT_PAGE = 1
    }
}
