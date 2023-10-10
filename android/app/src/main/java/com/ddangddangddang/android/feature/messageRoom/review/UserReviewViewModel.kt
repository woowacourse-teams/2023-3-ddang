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

    private var isLoading = false

    fun setPartnerInfo(detail: MessageRoomDetailModel) {
        partnerId = detail.messagePartnerId
        auctionId = detail.auctionId

        fetchReviewWritten()
    }

    private fun fetchReviewWritten() {
        viewModelScope.launch {
            if (isLoading) return@launch
            isLoading = true
            val auctionId = auctionId ?: return@launch
            when (val response = reviewRepository.getUserReview(auctionId)) {
                is ApiResponse.Success -> {
                    val score = response.body.score
                    val content = response.body.content
                    if (score != null && content != null) {
                        ratingGrade.value = score
                        reviewDetailContent.value = content
                        _isCompletedAlready.value = true
                    }
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
            isLoading = false
        }
    }

    fun submitReview() {
        val auctionId = auctionId ?: return
        val partnerId = partnerId ?: return

        val request = ReviewRequest(
            auctionId,
            partnerId,
            ratingGrade.value ?: 0f,
            reviewDetailContent.value ?: "",
        )
        viewModelScope.launch {
            if (isLoading) return@launch
            isLoading = true
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
            isLoading = false
        }
    }

    sealed class ReviewEvent {
        object ReviewSuccess : ReviewEvent()
        data class ReviewFailure(val error: ErrorType) : ReviewEvent()
        data class ReviewLoadFailure(val error: ErrorType) : ReviewEvent()
    }
}
