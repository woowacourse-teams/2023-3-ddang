package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportQuestionRequest(
    val auctionId: Long,
    val questionId: Long,
    val description: String,
)
