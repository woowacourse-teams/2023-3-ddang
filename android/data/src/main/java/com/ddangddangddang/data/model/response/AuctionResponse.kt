package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuctionResponse(
    val id: Long,
    val images: List<String>,
    val title: String,
    val category: CategoryResponse,
    val description: String,
    val startPrice: Int,
    val lastBidPrice: Int? = null,
    val status: String,
    val bidUnit: Int,
    val registerTime: String,
    val closingTime: String,
    val directRegions: List<DirectRegionResponse>,
    val auctioneerCount: Int,
) {
    fun toPreview(): AuctionPreviewResponse = AuctionPreviewResponse(
        id,
        title,
        images.firstOrNull() ?: "",
        lastBidPrice ?: startPrice,
        status,
        auctioneerCount,
    )
}
