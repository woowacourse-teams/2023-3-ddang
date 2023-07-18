package com.ddangddangddang.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuctionResponse(
    val id: Long,
    val images: List<String>,
    val title: String,
    val category: CategoryResponse,
    val description: String,
    val startPrice: Int,
    val lastBidPrice: Int,
    val status: String,
    val bidUnit: Int,
    val registerTime: String,
    val closingTime: String,
    val directRegions: List<RegionResponse>,
    val auctioneerCount: Int,
)
