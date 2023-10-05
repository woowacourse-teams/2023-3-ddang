package com.ddangddangddang.android.feature.messageRoom.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.MessageRoomDetailModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReviewViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {
    private var partnerId: Long? = null
    private var auctionId: Long? = null

    val ratingGrade = MutableLiveData(0.0)
    val reviewDetailContent = MutableLiveData("")

    private var _event: SingleLiveEvent<ReviewEvent> = SingleLiveEvent()
    val event: LiveData<ReviewEvent>
        get() = _event

    fun setPartnerInfo(detail: MessageRoomDetailModel) {
        partnerId = detail.messagePartnerId
        auctionId = detail.auctionId
    }

    fun submitReview() {
        val auctionId = auctionId ?: return
        val partnerId = partnerId ?: return

        val request = ReviewRequest(
            auctionId,
            partnerId,
            ratingGrade.value ?: 0.0,
            reviewDetailContent.value ?: "",
        )
        viewModelScope.launch {
            when (val response = reviewRepository.reviewUser(request)) {
                is ApiResponse.Success -> TODO()
                is ApiResponse.Failure -> TODO()
                is ApiResponse.NetworkError -> TODO()
                is ApiResponse.Unexpected -> TODO()
            }
        }
    }

    sealed class ReviewEvent {
        object ReviewSuccess : ReviewEvent()
        class ReviewFailure(error: ErrorType) : ReviewEvent()
    }
}
