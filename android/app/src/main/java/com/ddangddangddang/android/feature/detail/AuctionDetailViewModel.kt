package com.ddangddangddang.android.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.mapper.AuctionDetailModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch

class AuctionDetailViewModel(
    private val repository: AuctionRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<AuctionDetailEvent> = SingleLiveEvent()
    val event: LiveData<AuctionDetailEvent>
        get() = _event

    private val _auctionDetailModel: MutableLiveData<AuctionDetailModel> = MutableLiveData()
    val auctionDetailModel: LiveData<AuctionDetailModel>
        get() = _auctionDetailModel

    val auctionDetailBottomButtonStatus: LiveData<AuctionDetailBottomButtonStatus> =
        auctionDetailModel.map {
            AuctionDetailBottomButtonStatus.find(it)
        }

    val minBidPrice: Int
        get() {
            val auction = auctionDetailModel.value ?: return 0
            return auction.lastBidPrice + auction.bidUnit
        }

    fun loadAuctionDetail(auctionId: Long) {
        viewModelScope.launch {
            when (val response = repository.getAuctionDetail(auctionId)) {
                is ApiResponse.Success -> _auctionDetailModel.value = response.body.toPresentation()
                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun handleAuctionDetailBottomButton() {
        auctionDetailBottomButtonStatus.value?.let {
            when (it) {
                is AuctionDetailBottomButtonStatus.BidAuction -> popupAuctionBidEvent()
                is AuctionDetailBottomButtonStatus.CreateAuctionChatRoom -> createChatRoomEvent()
                is AuctionDetailBottomButtonStatus.EnterAuctionChatRoom -> enterChatRoomEvent()
                is AuctionDetailBottomButtonStatus.FinishAuction -> {}
            }
        }
    }

    private fun popupAuctionBidEvent() {
        _auctionDetailModel.value?.let {
            _event.value = AuctionDetailEvent.PopupAuctionBid
        }
    }

    private fun createChatRoomEvent() {
        _auctionDetailModel.value?.let {
            _event.value = AuctionDetailEvent.CreateChatRoom
        }
    }

    private fun enterChatRoomEvent() {
        _auctionDetailModel.value?.chatAuctionDetailModel?.id?.let {
            _event.value = AuctionDetailEvent.EnterChatRoom(it)
        }
    }

    fun createChatRoom() {
        _auctionDetailModel.value?.let {
            viewModelScope.launch {
            }
        }
    }

    fun exitEvent() {
        _event.value = AuctionDetailEvent.Exit
    }

    sealed class AuctionDetailEvent {
        object Exit : AuctionDetailEvent()
        object PopupAuctionBid : AuctionDetailEvent()
        object CreateChatRoom : AuctionDetailEvent()
        data class EnterChatRoom(val chatId: Long) : AuctionDetailEvent()
    }
}
