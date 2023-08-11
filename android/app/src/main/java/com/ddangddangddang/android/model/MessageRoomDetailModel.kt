package com.ddangddangddang.android.model

data class MessageRoomDetailModel(
    val roomId: Long,
    val auctionId: Long,
    val auctionTitle: String,
    val auctionImageUrl: String,
    val auctionPrice: Int,
    val messagePartnerId: Long,
    val messagePartnerName: String,
    val messagePartnerProfileUrl: String,
    val isChatAvailable: Boolean,
)
