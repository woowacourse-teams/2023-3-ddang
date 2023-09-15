package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.UserRemoteDataSource
import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.model.response.ProfileResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuctionService
import java.io.File

class UserRepositoryImpl(private val remoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun getProfile(): ApiResponse<ProfileResponse> = remoteDataSource.getProfile()
    override suspend fun updateProfile(
        image: File,
        profileUpdateRequest: ProfileUpdateRequest,
    ): ApiResponse<ProfileResponse> {
        return remoteDataSource.updateProfile(image, profileUpdateRequest)
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
