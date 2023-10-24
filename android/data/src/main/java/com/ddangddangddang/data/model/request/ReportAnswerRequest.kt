package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportAnswerRequest(
    val auctionId: Long,
    val answerId: Long,
    val description: String,
)
