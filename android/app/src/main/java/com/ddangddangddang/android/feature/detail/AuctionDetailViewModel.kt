package com.ddangddangddang.android.feature.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class AuctionDetailViewModel : ViewModel() {
    private val _event: SingleLiveEvent<AuctionDetailEvent> = SingleLiveEvent()
    val event: LiveData<AuctionDetailEvent>
        get() = _event

    private val _auctionDetailModel: MutableLiveData<AuctionDetailModel> = MutableLiveData()
    val auctionDetailModel: LiveData<AuctionDetailModel>
        get() = _auctionDetailModel

    fun exit() {
        _event.value = AuctionDetailEvent.Exit
    }

    sealed class AuctionDetailEvent {
        object Exit : AuctionDetailEvent()
    }
}
