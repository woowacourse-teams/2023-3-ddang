package com.ddangddangddang.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuctionDetailResponse(
    val auction: AuctionResponse,
    val seller: SellerResponse,
)
