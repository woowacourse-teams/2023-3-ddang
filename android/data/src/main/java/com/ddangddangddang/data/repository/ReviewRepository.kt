package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.remote.ApiResponse

interface ReviewRepository {
    suspend fun reviewUser(reviewRequest: ReviewRequest): ApiResponse<Unit>
}
