package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.model.response.UserReviewResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.remote.retrofit.AuctionService
import javax.inject.Inject

class ReviewRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun reviewUser(
        reviewRequest: ReviewRequest,
    ): ApiResponse<Unit> = service.reviewUser(reviewRequest)

    suspend fun getUserReview(auctionId: Long): ApiResponse<UserReviewResponse> =
        service.getReviewUser(auctionId)
}
