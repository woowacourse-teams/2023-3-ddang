package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService

class UserRemoteDataSource(private val service: AuctionService) {
    suspend fun getProfile(): ApiResponse<ProfileResponse> = service.fetchProfile()
}
