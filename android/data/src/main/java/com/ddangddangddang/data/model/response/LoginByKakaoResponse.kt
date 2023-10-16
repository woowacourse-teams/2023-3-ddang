package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginByKakaoResponse(
    val accessToken: String,
    val refreshToken: String,
    val isSignUpUser: Boolean,
) {
    fun toTokenResponse(): TokenResponse {
        return TokenResponse(
            accessToken,
            refreshToken,
        )
    }
}
