package com.ddangddangddang.data.repository

import com.ddangddangddang.data.datasource.AuthLocalDataSource
import com.ddangddangddang.data.datasource.AuthRemoteDataSource
import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.response.LoginByKakaoResponse
import com.ddangddangddang.data.model.response.TokenResponse
import com.ddangddangddang.data.model.response.ValidateTokenResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun loginByKakao(kakaoLoginRequest: KakaoLoginRequest): ApiResponse<LoginByKakaoResponse> {
        val response = remoteDataSource.loginByKakao(kakaoLoginRequest)
        if (response is ApiResponse.Success) {
            localDataSource.saveToken(response.body.toTokenResponse())
        }
        return response
    }

    override suspend fun refreshToken(): ApiResponse<TokenResponse> {
        val response =
            remoteDataSource.refreshToken(localDataSource.getRefreshToken())
        if (response is ApiResponse.Success) {
            localDataSource.saveToken(response.body)
        }
        return response
    }

    override fun getAccessToken(): String = localDataSource.getAccessToken()

    override fun getRefreshToken(): String = localDataSource.getRefreshToken()

    override suspend fun logout(): ApiResponse<Unit> {
        val response = remoteDataSource.logout(
            localDataSource.getAccessToken(),
            localDataSource.getRefreshToken(),
        )

        if (response is ApiResponse.Success) resetToken()

        return response
    }

    private fun resetToken() {
        val resetToken = TokenResponse(accessToken = "", refreshToken = "")
        localDataSource.saveToken(resetToken)
    }

    override suspend fun verifyToken(): ApiResponse<ValidateTokenResponse> =
        remoteDataSource.verifyToken(localDataSource.getAccessToken())

    override suspend fun getDeviceToken(): String? {
        return suspendCancellableCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result.toString())
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun withdrawal(): ApiResponse<Unit> {
        val response = remoteDataSource.withdrawal(
            localDataSource.getAccessToken(),
            localDataSource.getRefreshToken(),
        )

        if (response is ApiResponse.Success) resetToken()

        return response
    }
}
