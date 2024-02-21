package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.remote.retrofit.AuctionService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun getProfile(): ApiResponse<ProfileResponse> = service.fetchProfile()

    suspend fun updateProfile(
        image: File?,
        profileUpdateRequest: ProfileUpdateRequest,
    ): ApiResponse<ProfileResponse> {
        val fileBody = image?.let {
            MultipartBody.Part.createFormData(
                "profileImage",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull()),
            )
        }

        val body = Json.encodeToString(ProfileUpdateRequest.serializer(), profileUpdateRequest)
            .toRequestBody("application/json".toMediaType())
        return service.updateProfile(fileBody, body)
    }

    suspend fun getMyParticipateAuctionPreviews(
        page: Int,
        size: Int?,
    ): ApiResponse<AuctionPreviewsResponse> =
        service.getMyParticipateAuctionPreviews(page, size)

    suspend fun getMyAuctionPreviews(page: Int, size: Int?): ApiResponse<AuctionPreviewsResponse> =
        service.fetchMyAuctionPreviews(page, size)

    suspend fun updateDeviceToken(deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit> =
        service.updateDeviceToken(deviceTokenRequest)
}
