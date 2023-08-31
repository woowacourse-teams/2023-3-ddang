package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomPreviewResponse(
    val id: Long,
    val chatPartner: ChatPartnerResponse,
    val auction: ChatAuctionItemResponse,
    val lastMessage: ChatLastMessageResponse? = null,
    val isChatAvailable: Boolean,
)
