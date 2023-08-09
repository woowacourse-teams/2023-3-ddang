package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
