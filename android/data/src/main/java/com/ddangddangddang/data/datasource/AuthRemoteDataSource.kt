package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.request.LogoutRequest
import com.ddangddangddang.data.model.request.RefreshTokenRequest
import com.ddangddangddang.data.model.response.TokenResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuthService

class AuthRemoteDataSource(private val service: AuthService) {
    suspend fun loginByKakao(kakaoToken: KakaoLoginRequest): ApiResponse<TokenResponse> =
        service.loginByKakao(kakaoToken)

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): ApiResponse<TokenResponse> =
        service.refreshToken(refreshTokenRequest)

    suspend fun logout(accessToken: String, logoutRequest: LogoutRequest): ApiResponse<Unit> =
        service.logout(accessToken, logoutRequest)
}
