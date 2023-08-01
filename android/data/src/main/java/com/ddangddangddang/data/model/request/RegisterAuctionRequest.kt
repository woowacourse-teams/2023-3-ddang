package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterAuctionRequest(
    val title: String,
    val subCategoryId: Long,
    val description: String,
    val startPrice: Int,
    val bidUnit: Int,
    val closingTime: String,
    val thirdRegionIds: List<Long>,
)
