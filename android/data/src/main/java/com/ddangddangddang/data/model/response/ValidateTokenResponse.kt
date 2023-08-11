package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ValidateTokenResponse(
    val validated: Boolean,
)
