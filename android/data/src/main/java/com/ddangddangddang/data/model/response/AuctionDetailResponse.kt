package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuctionDetailResponse(
    val auction: AuctionResponse,
    val seller: SellerResponse,
    val chat: ChatAuctionDetailResponse,
    val isOwner: Boolean,
)
