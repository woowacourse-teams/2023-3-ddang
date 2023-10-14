package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AskQuestionRequest(
    val auctionId: Long,
    val content: String,
)
