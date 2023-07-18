package com.ddangddangddang.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SellerResponse(
    val id: Long,
    val image: String,
    val nickname: String,
    val reliability: Double,
)
