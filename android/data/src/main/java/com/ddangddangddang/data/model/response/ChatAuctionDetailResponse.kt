package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatAuctionDetailResponse(
    val id: Long? = null,
    val isChatParticipant: Boolean,
)
