package com.ddangddangddang.data.remote

import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.model.request.LogoutRequest
import com.ddangddangddang.data.model.request.RefreshTokenRequest
import com.ddangddangddang.data.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/oauth2/login/kakao")
    suspend fun loginByKakao(
        @Body kakaoLoginRequest: KakaoLoginRequest,
    ): ApiResponse<TokenResponse>

    @POST("/oauth2/refresh-token")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest,
    ): ApiResponse<TokenResponse>

    @POST("/oauth2/logout")
    suspend fun logout(
        @Header("Authorization") authorization: String,
        @Body logoutRequest: LogoutRequest,
    ): ApiResponse<Unit>
}
