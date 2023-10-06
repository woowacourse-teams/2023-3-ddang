package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.UserRemoteDataSource
import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.ApiResponse
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val remoteDataSource: UserRemoteDataSource) :
    UserRepository {
    override suspend fun getProfile(): ApiResponse<ProfileResponse> = remoteDataSource.getProfile()
    override suspend fun updateProfile(
        image: File?,
        profileUpdateRequest: ProfileUpdateRequest,
    ): ApiResponse<ProfileResponse> {
        return remoteDataSource.updateProfile(image, profileUpdateRequest)
    }

    override suspend fun getMyParticipateAuctionPreviews(
        page: Int,
        size: Int?,
    ): ApiResponse<AuctionPreviewsResponse> {
        return remoteDataSource.getMyParticipateAuctionPreviews(page, size)
    }

    override suspend fun getMyAuctionPreviews(
        page: Int,
        size: Int?,
    ): ApiResponse<AuctionPreviewsResponse> {
        return remoteDataSource.getMyAuctionPreviews(page, size)
    }

    override suspend fun updateDeviceToken(deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit> {
        return remoteDataSource.updateDeviceToken(deviceTokenRequest)
    }
}
