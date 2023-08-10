package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String,
)
