package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.ReviewRemoteDataSource
import com.ddangddangddang.data.model.request.ReviewRequest
import com.ddangddangddang.data.remote.ApiResponse
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val remoteDataSource: ReviewRemoteDataSource) :
    ReviewRepository {
    override suspend fun reviewUser(reviewRequest: ReviewRequest): ApiResponse<Unit> =
        remoteDataSource.reviewUser(reviewRequest)
}
