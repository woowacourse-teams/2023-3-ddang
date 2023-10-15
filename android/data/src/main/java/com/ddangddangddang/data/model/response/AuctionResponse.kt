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
    val hasLastBidder: Boolean? = false, // 아직 api 수정 안돼서 일단 이걸로 설정함.
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
