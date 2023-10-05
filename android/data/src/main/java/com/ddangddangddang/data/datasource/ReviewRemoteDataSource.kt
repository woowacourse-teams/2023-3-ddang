package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService
import javax.inject.Inject

class ReviewRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun reviewUser(
        reviewRequest: ReviewRequest,
    ): ApiResponse<Unit> = service.reviewUser(reviewRequest)
}
