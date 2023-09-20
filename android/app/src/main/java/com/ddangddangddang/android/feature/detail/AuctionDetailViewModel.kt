package com.ddangddangddang.android.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.mapper.AuctionDetailModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.GetChatRoomIdRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import com.ddangddangddang.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class AuctionDetailViewModel(
    private val auctionRepository: AuctionRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<AuctionDetailEvent> = SingleLiveEvent()
    val event: LiveData<AuctionDetailEvent>
        get() = _event

    private val _isLoadingWithAnimation: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingWithAnimation: LiveData<Boolean>
        get() = _isLoadingWithAnimation

    private var isLoadingWithoutAnimation: Boolean = false

    private val _auctionDetailModel: MutableLiveData<AuctionDetailModel> = MutableLiveData()
    val auctionDetailModel: LiveData<AuctionDetailModel>
        get() = _auctionDetailModel

    val auctionDetailBottomButtonStatus: LiveData<AuctionDetailBottomButtonStatus> =
        _auctionDetailModel.map {
            AuctionDetailBottomButtonStatus.find(it)
        }

    val minBidPrice: Int
        get() {
            val auction = auctionDetailModel.value ?: return 0
            if (auction.auctioneerCount == 0) return auction.lastBidPrice
            return auction.lastBidPrice + auction.bidUnit
        }

    fun loadAuctionDetail(auctionId: Long) {
        if (_isLoadingWithAnimation.value == true) return
        _isLoadingWithAnimation.value = true
        viewModelScope.launch {
            when (val response = auctionRepository.getAuctionDetail(auctionId)) {
                is ApiResponse.Success -> _auctionDetailModel.value = response.body.toPresentation()
                is ApiResponse.Failure -> {
                    if (response.responseCode == 404) {
                        _event.value = AuctionDetailEvent.NotifyAuctionDoesNotExist
                    }
                }

                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
            _isLoadingWithAnimation.value = false
        }
    }

    fun handleAuctionDetailBottomButton() {
        auctionDetailBottomButtonStatus.value?.let {
            when (it) {
                AuctionDetailBottomButtonStatus.BidAuction -> popupAuctionBidEvent()
                AuctionDetailBottomButtonStatus.EnterAuctionChatRoom -> enterChatRoomEvent()
                AuctionDetailBottomButtonStatus.FinishAuction -> {}
                AuctionDetailBottomButtonStatus.MyAuction -> {}
            }
        }
    }

    private fun popupAuctionBidEvent() {
        _auctionDetailModel.value?.let {
            _event.value = AuctionDetailEvent.PopupAuctionBid
        }
    }

    private fun enterChatRoomEvent() {
        _auctionDetailModel.value?.let {
            if (isLoadingWithoutAnimation) return@let
            isLoadingWithoutAnimation = true
            viewModelScope.launch {
                val request = GetChatRoomIdRequest(it.id)
                when (val response = chatRepository.getChatRoomId(request)) {
                    is ApiResponse.Success -> {
                        _event.value = AuctionDetailEvent.EnterMessageRoom(response.body.chatRoomId)
                    }

                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
                }
                isLoadingWithoutAnimation = false
            }
        }
    }

    fun setExitEvent() {
        _event.value = AuctionDetailEvent.Exit
    }

    fun reportAuction() {
        auctionDetailModel.value?.let {
            _event.value = AuctionDetailEvent.ReportAuction(it.id)
        }
    }

    fun setDeleteAuctionEvent() {
        auctionDetailModel.value?.let {
            _event.value = AuctionDetailEvent.DeleteAuction
        }
    }

    fun deleteAuction() {
        _auctionDetailModel.value?.let {
            if (isLoadingWithoutAnimation) return@let
            isLoadingWithoutAnimation = true
            viewModelScope.launch {
                when (auctionRepository.deleteAuction(it.id)) {
                    is ApiResponse.Success ->
                        _event.value =
                            AuctionDetailEvent.NotifyAuctionDeletionComplete

                    is ApiResponse.Failure -> {}
                    is ApiResponse.NetworkError -> {}
                    is ApiResponse.Unexpected -> {}
                }
                isLoadingWithoutAnimation = false
            }
        }
    }

    sealed class AuctionDetailEvent {
        object Exit : AuctionDetailEvent()
        object PopupAuctionBid : AuctionDetailEvent()
        data class EnterMessageRoom(val roomId: Long) : AuctionDetailEvent()
        data class ReportAuction(val auctionId: Long) : AuctionDetailEvent()
        object DeleteAuction : AuctionDetailEvent()
        object NotifyAuctionDoesNotExist : AuctionDetailEvent()
        object NotifyAuctionDeletionComplete : AuctionDetailEvent()
    }
}
