package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SellerResponse(
    val id: Long,
    val image: String,
    val nickname: String,
    val reliability: Double,
)
