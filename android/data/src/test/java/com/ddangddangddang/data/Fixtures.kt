package com.ddangddangddang.data

import com.ddangddangddang.data.model.request.CategoryRequest
import com.ddangddangddang.data.model.request.DirectRegionRequest
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse

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

fun createRegisterAuctionRequest() = RegisterAuctionRequest(
    listOf("https://i0.wp.com/opensea.kr/wp-content/uploads/2021/01/2169FF1C-8A4A-46CE-A55A-7ED291EEA033_1_105_c-1.jpeg?w=1051&ssl=1"),
    "맥북 에어 13인치",
    CategoryRequest("전자기기", "노트북"),
    "얼마 안쓴 맥북 팝니다",
    100000,
    1000,
    "2100-07-31T12:00:00",
    listOf(DirectRegionRequest("경기도", "부천시", "원미구")),
)

fun createRegisterAuctionResponse() = RegisterAuctionResponse(2)
