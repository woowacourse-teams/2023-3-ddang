package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.request.LogoutRequest
import com.ddangddangddang.data.model.request.RefreshTokenRequest
import com.ddangddangddang.data.model.request.WithdrawalRequest
import com.ddangddangddang.data.model.response.LoginByKakaoResponse
import com.ddangddangddang.data.model.response.TokenResponse
import com.ddangddangddang.data.model.response.ValidateTokenResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.AuthService
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val service: AuthService) {
    suspend fun loginByKakao(kakaoLoginRequest: KakaoLoginRequest): ApiResponse<LoginByKakaoResponse> =
        service.loginByKakao(kakaoLoginRequest)

    suspend fun refreshToken(refreshToken: String): ApiResponse<TokenResponse> =
        service.refreshToken(RefreshTokenRequest(refreshToken))

    suspend fun logout(accessToken: String, refreshToken: String): ApiResponse<Unit> {
        return service.logout(accessToken, LogoutRequest(refreshToken))
    }

    suspend fun verifyToken(token: String): ApiResponse<ValidateTokenResponse> =
        service.verifyToken(token)

    suspend fun withdrawal(accessToken: String, refreshToken: String): ApiResponse<Unit> {
        return service.withdrawal(accessToken, WithdrawalRequest(refreshToken))
    }
}
