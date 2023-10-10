package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.model.response.UserReviewResponse
import com.ddangddangddang.data.remote.ApiResponse

interface ReviewRepository {
    suspend fun reviewUser(reviewRequest: ReviewRequest): ApiResponse<Unit>
    suspend fun getUserReview(auctionId: Long): ApiResponse<UserReviewResponse>
}
