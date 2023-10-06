package com.ddangddangddang.android.model

data class MessageModel(
    val id: Long,
    val createdDateTime: String,
    val isMyMessage: Boolean,
    val contents: String,
)
