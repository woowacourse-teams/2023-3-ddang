package com.ddangddangddang.android

import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.AuctionDetailStatusModel
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel
import com.ddangddangddang.android.model.RegionModel
import com.ddangddangddang.android.model.SellerModel
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import com.ddangddangddang.data.model.response.AuctionPreviewResponse
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse
import com.ddangddangddang.data.model.response.AuctionResponse
import com.ddangddangddang.data.model.response.CategoryResponse
import com.ddangddangddang.data.model.response.DirectRegionResponse
import com.ddangddangddang.data.model.response.SellerResponse
import java.time.LocalDateTime

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

fun createAuctionHomeModel(
    id: Long = 1,
    title: String = "맥북 2021 13인치",
    image: String = "https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg",
    auctionPrice: Int = 1000000,
    status: AuctionHomeStatusModel = AuctionHomeStatusModel.FAILURE,
    auctioneerCount: Int = 0,
) = AuctionHomeModel(id, title, image, auctionPrice, status, auctioneerCount)

fun createAuctionDetailResponse(
    auctionResponse: AuctionResponse = createAuctionResponse(),
    sellerResponse: SellerResponse = createSellerResponse(),
) = AuctionDetailResponse(auctionResponse, sellerResponse)

fun createAuctionResponse(
    id: Long = 1L,
    images: List<String> = listOf("https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg"),
    title: String = "맥북 2021 13인치",
    category: CategoryResponse = CategoryResponse("전자기기", "노트북"),
    description: String = "",
    startPrice: Int = 800000,
    lastBidPrice: Int? = null,
    status: String = "UNBIDDEN",
    bidUnit: Int = 10000,
    registerTime: String = "2023-07-20T12:34:56",
    closingTime: String = "2023-07-21T12:34:56",
    directRegions: List<DirectRegionResponse> = listOf(),
    auctioneerCount: Int = 0,
) = AuctionResponse(
    id,
    images,
    title,
    category,
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

fun createSellerResponse(
    id: Long = 1L,
    image: String = "",
    nickname: String = "test_seller",
    reliability: Double = 5.0,
) = SellerResponse(id, image, nickname, reliability)

fun createAuctionDetailModel(
    id: Long = 1L,
    images: List<String> = listOf("https://www.apple.com/newsroom/images/product/mac/standard/Apple_MacBook-Pro_14-16-inch_10182021_big.jpg.large.jpg"),
    title: String = "맥북 2021 13인치",
    mainCategory: String = "전자기기",
    subCategory: String = "노트북",
    description: String = "",
    startPrice: Int = 800000,
    lastBidPrice: Int = 800000,
    auctionDetailStatusModel: AuctionDetailStatusModel = AuctionDetailStatusModel.UNBIDDEN,
    bidUnit: Int = 10000,
    registerTime: LocalDateTime = LocalDateTime.of(2023, 7, 20, 12, 34, 56),
    closingTime: LocalDateTime = LocalDateTime.of(2023, 7, 21, 12, 34, 56),
    directRegions: List<RegionModel> = listOf(),
    auctioneerCount: Int = 0,
    sellerModel: SellerModel = createSellerModel(),
) = AuctionDetailModel(
    id,
    images,
    title,
    mainCategory,
    subCategory,
    description,
    startPrice,
    lastBidPrice,
    auctionDetailStatusModel,
    bidUnit,
    registerTime,
    closingTime,
    directRegions,
    auctioneerCount,
    sellerModel,
)

fun createSellerModel(
    id: Long = 1L,
    profileUrl: String = "",
    nickname: String = "test_seller",
    reliability: Double = 5.0,
) = SellerModel(id, profileUrl, nickname, reliability)
