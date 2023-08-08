package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.AuthLocalDataSource
import com.ddangddangddang.data.datasource.AuthRemoteDataSource
import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.response.TokenResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class AuthRepositoryImpl private constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun loginByKakao(kakaoToken: KakaoLoginRequest): ApiResponse<TokenResponse> {
        val response = remoteDataSource.loginByKakao(kakaoToken)
        return response
    }

    companion object {
        @Volatile
        private var instance: AuthRepositoryImpl? = null

        fun getInstance(service: Service): AuthRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: createInstance(service)
            }
        }

        private fun createInstance(service: Service): AuthRepositoryImpl {
            val localDataSource = AuthLocalDataSource()
            val remoteDataSource = AuthRemoteDataSource(service)
            return AuthRepositoryImpl(localDataSource, remoteDataSource)
                .also { instance = it }
        }
    }
}
