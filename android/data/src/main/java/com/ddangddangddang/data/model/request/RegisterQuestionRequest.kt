package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterQuestionRequest(
    val auctionId: Long,
    val content: String,
)
