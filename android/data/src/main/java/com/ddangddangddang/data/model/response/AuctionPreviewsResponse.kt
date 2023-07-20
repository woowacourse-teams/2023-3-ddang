package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuctionPreviewsResponse(
    val auctions: List<AuctionPreviewResponse>,
    val lastAuctionId: Long? = null,
)
