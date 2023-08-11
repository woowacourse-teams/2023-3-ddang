package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.AuctionDetailModel
import com.ddangddangddang.android.model.AuctionDetailStatusModel
import com.ddangddangddang.android.model.ChatAuctionDetailModel
import com.ddangddangddang.android.model.RegionModel
import com.ddangddangddang.android.model.SellerModel
import com.ddangddangddang.data.model.response.AuctionDetailResponse
import java.time.LocalDateTime

object AuctionDetailModelMapper : Mapper<AuctionDetailModel, AuctionDetailResponse> {
    override fun AuctionDetailResponse.toPresentation(): AuctionDetailModel {
        return AuctionDetailModel(
            auction.id,
            auction.images,
            auction.title,
            auction.category.main,
            auction.category.sub,
            auction.description,
            auction.startPrice,
            auction.lastBidPrice ?: auction.startPrice,
            AuctionDetailStatusModel.find(auction.status),
            auction.bidUnit,
            LocalDateTime.parse(auction.registerTime),
            LocalDateTime.parse(auction.closingTime),
            auction.directRegions.map {
                RegionModel(
                    it.first,
                    it.second.ifEmpty { null },
                    it.third.ifEmpty { null },
                )
            },
            auction.auctioneerCount,
            SellerModel(seller.id, seller.image ?: "", seller.nickname, seller.reliability),
            ChatAuctionDetailModel(chat.id, chat.isChatParticipant),
        )
    }
}
