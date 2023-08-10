package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    val receiverId: Long,
    val contents: String,
)
