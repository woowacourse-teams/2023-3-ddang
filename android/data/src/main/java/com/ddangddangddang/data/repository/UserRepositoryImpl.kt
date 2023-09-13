package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.UserRemoteDataSource
import com.ddangddangddang.data.model.request.UpdateDeviceTokenRequest
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService

class UserRepositoryImpl(private val remoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun getProfile(): ApiResponse<ProfileResponse> = remoteDataSource.getProfile()

    override suspend fun updateDeviceToken(deviceTokenRequest: UpdateDeviceTokenRequest): ApiResponse<Unit> {
        return remoteDataSource.updateDeviceToken(deviceTokenRequest)
    }

    companion object {
        @Volatile
        private var instance: UserRepositoryImpl? = null

        fun getInstance(service: AuctionService): UserRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: AuctionService): UserRepositoryImpl {
            return UserRepositoryImpl(UserRemoteDataSource(service)).also { instance = it }
        }
    }
}
