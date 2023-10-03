package com.ddangddangddang.android.feature.messageRoom.rate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.MessageRoomDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRateViewModel @Inject constructor() : ViewModel() {
    private var partnerId: Long? = null
    private var auctionId: Long? = null
    private var ratingGrade: Float = 0F

    val rateDetailContent = MutableLiveData<String>("")

    fun setPartnerInfo(detail: MessageRoomDetailModel) {
        partnerId = detail.messagePartnerId
        auctionId = detail.auctionId
    }

    fun setRatingGrade(grade: Float) {
        ratingGrade = grade
        Log.d("test - grade", ratingGrade.toString())
    }

    fun submitRate() {
        // submit
        Log.d("test", "submit")
    }
}
