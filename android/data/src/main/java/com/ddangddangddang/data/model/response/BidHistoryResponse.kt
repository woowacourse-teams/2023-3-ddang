package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class BidHistoryResponse(
    val name: String,
    val profileImage: String,
    val price: Int,
    val bidTime: String,
)
