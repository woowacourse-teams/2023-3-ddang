package com.ddangddangddang.android.feature.participateAuction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipateAuctionViewModel @Inject constructor(
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

    fun loadMyParticipateAuctions() {
        fetchAuctions(_page + 1)
    }

    fun reloadAuctions() {
        fetchAuctions(DEFAULT_PAGE)
    }

    private fun fetchAuctions(newPage: Int) {
        if (loadingAuctionInProgress) return
        _loadingAuctionsInProgress = true
        viewModelScope.launch {
            when (
                val response =
                    userRepository.getMyParticipateAuctionPreviews(newPage, SIZE_AUCTION_LOAD)
            ) {
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
        val newItems = response.auctions.map { it.toPresentation() }
        _auctions.value = if (newPage == 1) {
            newItems
        } else {
            (_auctions.value ?: emptyList()) + newItems
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
        private const val SIZE_AUCTION_LOAD = 20
        private const val DEFAULT_PAGE = 1
    }
}
