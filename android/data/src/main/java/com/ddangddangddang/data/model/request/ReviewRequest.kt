package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    val auctionId: Long,
    val targetId: Long,
    val score: Float,
    val content: String,
)
