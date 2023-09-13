package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.ApiResponse

interface UserRepository {
    suspend fun getProfile(): ApiResponse<ProfileResponse>

    suspend fun updateDeviceToken(deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit>
}
