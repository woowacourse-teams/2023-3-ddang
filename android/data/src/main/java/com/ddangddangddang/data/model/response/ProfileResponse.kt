package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val name: String,
    val profileImage: String?,
    val reliability: Double,
)
