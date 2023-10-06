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

    val ratingGrade = MutableLiveData(0f)
    val reviewDetailContent = MutableLiveData("")

    private var _isCompletedAlready = MutableLiveData(false)
    val isCompletedAlready: LiveData<Boolean>
        get() = _isCompletedAlready

    private var _event: SingleLiveEvent<ReviewEvent> = SingleLiveEvent()
    val event: LiveData<ReviewEvent>
        get() = _event

    fun setPartnerInfo(detail: MessageRoomDetailModel) {
        partnerId = detail.messagePartnerId
        auctionId = detail.auctionId

        fetchReviewWritten()
    }

    private fun fetchReviewWritten() {
        viewModelScope.launch {
            val auctionId = auctionId ?: return@launch
            when (val response = reviewRepository.getUserReview(auctionId)) {
                is ApiResponse.Success -> {
                    ratingGrade.value = response.body.score.toFloat()
                    reviewDetailContent.value = response.body.content
                    _isCompletedAlready.value = true
                }
                is ApiResponse.Failure -> {
                    _event.value = ReviewEvent.ReviewLoadFailure(ErrorType.FAILURE(response.error))
                }
                is ApiResponse.NetworkError -> {
                    _event.value = ReviewEvent.ReviewLoadFailure(ErrorType.NETWORK_ERROR)
                }
                is ApiResponse.Unexpected -> {
                    _event.value = ReviewEvent.ReviewLoadFailure(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun submitReview() {
        val auctionId = auctionId ?: return
        val partnerId = partnerId ?: return

        val request = ReviewRequest(
            auctionId,
            partnerId,
            ratingGrade.value?.toDouble() ?: 0.0,
            reviewDetailContent.value ?: "",
        )
        viewModelScope.launch {
            when (val response = reviewRepository.reviewUser(request)) {
                is ApiResponse.Success -> {
                    _event.value = ReviewEvent.ReviewSuccess
                }
                is ApiResponse.Failure -> {
                    _event.value = ReviewEvent.ReviewFailure(ErrorType.FAILURE(response.error))
                }
                is ApiResponse.NetworkError -> {
                    _event.value = ReviewEvent.ReviewFailure(ErrorType.NETWORK_ERROR)
                }
                is ApiResponse.Unexpected -> {
                    _event.value = ReviewEvent.ReviewFailure(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    sealed class ReviewEvent {
        object ReviewSuccess : ReviewEvent()
        class ReviewFailure(val error: ErrorType) : ReviewEvent()
        class ReviewLoadFailure(val error: ErrorType) : ReviewEvent()
    }
}
