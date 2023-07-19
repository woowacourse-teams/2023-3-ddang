package com.ddangddangddang.android.model

data class AuctionHomeModel(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val auctionPrice: Int,
    val status: AuctionHomeStatusModel,
    val auctioneerCount: Int,
)
