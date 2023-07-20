package com.ddangddangddang.data

import com.ddangddangddang.data.model.request.CategoryRequest
import com.ddangddangddang.data.model.request.DirectRegionRequest
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.AuctionResponse
import com.ddangddangddang.data.model.response.CategoryResponse
import com.ddangddangddang.data.model.response.DirectRegionResponse
import com.ddangddangddang.data.model.response.RegisterAuctionResponse
import com.ddangddangddang.data.model.response.SellerResponse

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

fun createRegisterAuctionRequest(
    images: List<String> = listOf("https://i0.wp.com/opensea.kr/wp-content/uploads/2021/01/2169FF1C-8A4A-46CE-A55A-7ED291EEA033_1_105_c-1.jpeg?w=1051&ssl=1"),
    title: String = "맥북 에어 13인치",
    category: CategoryRequest = CategoryRequest("전자기기", "노트북"),
    description: String = "얼마 안쓴 맥북 팝니다",
    startPrice: Int = 100000,
    bidUnit: Int = 1000,
    closingTime: String = "2100-07-31T12:00:00",
    directRegions: List<DirectRegionRequest> = listOf(DirectRegionRequest("경기도", "부천시", "원미구")),
) = RegisterAuctionRequest(
    images,
    title,
    category,
    description,
    startPrice,
    bidUnit,
    closingTime,
    directRegions,
)

fun createRegisterAuctionResponse(id: Long = 2) = RegisterAuctionResponse(id)
fun createAuctionDetailResponse(
    auction: AuctionResponse = createAuctionResponse(),
    seller: SellerResponse = createSellerResponse(),
) = AuctionDetailResponse(auction, seller)

private fun createAuctionResponse(
    id: Long = 2,
    images: List<String> = listOf("https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg"),
    title: String = "맥북 16인치",
    categoryResponse: CategoryResponse = CategoryResponse("전자기기", "노트북"),
    description: String = "맥북 2019 16인치 팝니다. 급전이 필요해요...",
    startPrice: Int = 900000,
    lastBidPrice: Int = 100000,
    status: String = "UNBIDDEN",
    bidUnit: Int = 3000,
    registerTime: String = "2023-07-20T16:38:28",
    closingTime: String = "2023-07-25T16:38:28",
    directRegions: List<DirectRegionResponse> = listOf(DirectRegionResponse("서울특별시", "관악구", "성현동")),
    auctioneerCount: Int = 0,
) = AuctionResponse(
    id,
    images,
    title,
    categoryResponse,
    description,
    startPrice,
    lastBidPrice,
    status,
    bidUnit,
    registerTime,
    closingTime,
    directRegions,
    auctioneerCount,
)

private fun createSellerResponse(
    id: Long = 1,
    image: String = "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
    nickname: String = "닉네임",
    reliability: Double = 5.0,
) = SellerResponse(id, image, nickname, reliability)
