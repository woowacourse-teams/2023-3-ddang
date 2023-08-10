package com.ddangddangddang.android.model

import java.time.LocalDateTime

data class MessageRoomModel(
    val roomId: Long,
    val auctionTitle: String,
    val partnerProfileUrl: String,
    val partnerName: String,
    val lastMessageContents: String,
    val lastMessageDateTime: LocalDateTime,
    val isChatAvailable: Boolean,
)
