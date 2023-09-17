package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserRemoteDataSource(private val service: AuctionService) {
    suspend fun getProfile(): ApiResponse<ProfileResponse> = service.fetchProfile()

    suspend fun updateProfile(
        image: File,
        profileUpdateRequest: ProfileUpdateRequest,
    ): ApiResponse<ProfileResponse> {
        val fileBody = MultipartBody.Part.createFormData(
            "profileImage",
            image.name,
            image.asRequestBody("image/*".toMediaTypeOrNull()),
        )

        val body = Json.encodeToString(ProfileUpdateRequest.serializer(), profileUpdateRequest)
            .toRequestBody("application/json".toMediaType())
        return service.updateProfile(fileBody, body)
    }

    suspend fun getMyAuctionPreviews(): ApiResponse<AuctionPreviewsResponse> =
        service.fetchMyAuctionPreviews()

    suspend fun updateDeviceToken(deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit> =
        service.updateDeviceToken(deviceTokenRequest)
}
