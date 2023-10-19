package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.response.LoginByKakaoResponse
import com.ddangddangddang.data.model.response.TokenResponse
import com.ddangddangddang.data.model.response.ValidateTokenResponse
import com.ddangddangddang.data.remote.ApiResponse

interface AuthRepository {
    suspend fun loginByKakao(kakaoLoginRequest: KakaoLoginRequest): ApiResponse<LoginByKakaoResponse>

    suspend fun refreshToken(): ApiResponse<TokenResponse>

    fun getAccessToken(): String

    fun getRefreshToken(): String

    suspend fun logout(): ApiResponse<Unit>

    suspend fun verifyToken(): ApiResponse<ValidateTokenResponse>

    suspend fun getDeviceToken(): String?

    suspend fun withdrawal(): ApiResponse<Unit>
}
