package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.AuctionDetailStatusModel
import com.ddangddangddang.android.model.RegionModel
import com.ddangddangddang.android.model.SellerModel
import com.ddangddangddang.data.model.AuctionDetailResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

fun AuctionDetailResponse.toPresentation(): AuctionDetailModel {
    return AuctionDetailModel(
        auction.id,
        auction.images,
        auction.title,
        auction.category.main,
        auction.category.sub,
        auction.description,
        auction.startPrice,
        auction.lastBidPrice,
        AuctionDetailStatusModel.findStatus(auction.status),
        auction.bidUnit,
        LocalDateTime.parse(auction.registerTime, formatter),
        LocalDateTime.parse(auction.closingTime, formatter),
        auction.directRegions.map { RegionModel(it.first, it.second, it.third) },
        auction.auctioneerCount,
        SellerModel(seller.id, seller.image, seller.nickname, seller.reliability),
    )
}
