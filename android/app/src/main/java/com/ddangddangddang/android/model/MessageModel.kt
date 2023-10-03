package com.ddangddangddang.android.model

data class MessageModel(
    val id: Long,
    val createdAt: String,
    val isMyMessage: Boolean,
    val contents: String,
)
