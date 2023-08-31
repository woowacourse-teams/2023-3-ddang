package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatLastMessageResponse(
    val contents: String,
    val createdAt: String,
)
