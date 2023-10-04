package com.ddangddangddang.android.model

import java.time.LocalDateTime

data class MessageModel(
    val id: Long,
    val createdDateTime: LocalDateTime,
    val isMyMessage: Boolean,
    val contents: String,
)
