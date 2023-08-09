package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatAuctionItemResponse(
    val id: Long,
    val title: String,
    val image: String,
    val price: Int,
)
