package com.ddangddangddang.data.model

data class AuctionRequest(
    val imageUrl: List<String>,
    val title: String,
    val category: Category,
    val description: String,
    val startPrice: Int,
    val bidUnit: Int,
    val closingTime: String,
    val directRegion: List<DirectRegion>,
) {
    data class Category(val main: Int, val sub: Int)
    data class DirectRegion(val first: Int, val second: Int, val third: Int)
}
