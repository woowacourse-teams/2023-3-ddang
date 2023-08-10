package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    val id: Long,
    val createdAt: String,
    val isMyMessage: Boolean,
    val contents: String,
)
