package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuctionPreviewResponse(
    val id: Long,
    val title: String,
    val image: String,
    val auctionPrice: Int,
    val status: String,
    val auctioneerCount: Int,
)
