package com.ddangddangddang.android.model

data class MessageRoomModel(
    val roomId: Long,
    val auctionTitle: String,
    val partnerProfileUrl: String,
    val partnerName: String,
    val lastMessageContents: String,
    val lastMessageDateTime: String,
    val unreadMessageCount: String,
    val isChatAvailable: Boolean,
)
