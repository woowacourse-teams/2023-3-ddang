package com.ddangddangddang.android.model

import java.time.LocalDateTime

data class AuctionDetailModel(
    val id: Long,
    val images: List<String>,
    val title: String,
    val mainCategory: String,
    val subCategory: String,
    val description: String,
    val startPrice: Int,
    val lastBidPrice: Int,
    val auctionDetailStatusModel: AuctionDetailStatusModel,
    val bidUnit: Int,
    val registerTime: LocalDateTime,
    val closingTime: LocalDateTime,
    val directRegions: List<RegionModel>,
    val auctioneerCount: Int,
    val sellerModel: SellerModel,
    val chatAuctionDetailModel: ChatAuctionDetailModel,
    val isOwner: Boolean,
    val hasLastBidder: Boolean,
)
