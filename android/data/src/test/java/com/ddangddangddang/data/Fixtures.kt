package com.ddangddangddang.data

import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse

private fun createAuctionPreviewResponse(
    id: Long = 1,
    title: String = "맥북 2021 13인치",
    image: String = "https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg",
    auctionPrice: Int = 1000000,
    status: String = "FAILURE",
    auctioneerCount: Int = 0,
) = AuctionPreviewResponse(id, title, image, auctionPrice, status, auctioneerCount)

fun createAuctionPreviewsResponse(
    auctions: List<AuctionPreviewResponse> = listOf(createAuctionPreviewResponse()),
    lastAuctionId: Long = 1,
) = AuctionPreviewsResponse(auctions, lastAuctionId)
