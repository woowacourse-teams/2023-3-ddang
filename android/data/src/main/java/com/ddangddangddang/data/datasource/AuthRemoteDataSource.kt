package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.request.LogoutRequest
import com.ddangddangddang.data.model.request.RefreshTokenRequest
import com.ddangddangddang.data.model.response.TokenResponse
import com.ddangddangddang.data.model.response.ValidateTokenResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuthService

class AuthRemoteDataSource(private val service: AuthService) {
    suspend fun loginByKakao(kakaoLoginRequest: KakaoLoginRequest): ApiResponse<TokenResponse> =
        service.loginByKakao(kakaoLoginRequest)

    suspend fun refreshToken(refreshToken: String): ApiResponse<TokenResponse> =
        service.refreshToken(RefreshTokenRequest(formatToken(refreshToken)))

    suspend fun logout(accessToken: String, refreshToken: String): ApiResponse<Unit> {
        val response =
            service.logout(formatToken(accessToken), LogoutRequest(formatToken(refreshToken)))
        return response
    }

    suspend fun verifyToken(token: String): ApiResponse<ValidateTokenResponse> =
        service.verifyToken(formatToken(token))

    private fun formatToken(token: String): String = "$TOKEN_PREFIX $token"

    companion object {
        private const val TOKEN_PREFIX = "Bearer"
    }
}
