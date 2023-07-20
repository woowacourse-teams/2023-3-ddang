package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterAuctionRequest(
    val images: List<String>,
    val title: String,
    val category: CategoryRequest,
    val description: String,
    val startPrice: Int,
    val bidUnit: Int,
    val closingTime: String,
    val directRegions: List<DirectRegionRequest>,
)
