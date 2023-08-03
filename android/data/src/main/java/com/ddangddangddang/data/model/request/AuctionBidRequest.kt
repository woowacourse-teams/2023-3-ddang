package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuctionBidRequest(
    val auctionId: Long,
    val bidPrice: Int,
)
