package com.ddangddangddang.android.feature.myAuction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.mapper.AuctionHomeModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
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

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    fun navigateToAuctionDetail(auctionId: Long) {
        _event.value = Event.NavigateToAuctionDetail(auctionId)
    }

    fun loadMyAuctions() {
        viewModelScope.launch {
            when (val response = userRepository.getMyAuctionPreviews()) {
                is ApiResponse.Success -> {
                    _auctions.value = response.body.auctions.map { it.toPresentation() }
                }

                is ApiResponse.Failure -> TODO()
                is ApiResponse.NetworkError -> TODO()
                is ApiResponse.Unexpected -> TODO()
            }
        }
    }

    sealed class Event {
        object Exit : Event()
        data class NavigateToAuctionDetail(val auctionId: Long) : Event()
    }
}
