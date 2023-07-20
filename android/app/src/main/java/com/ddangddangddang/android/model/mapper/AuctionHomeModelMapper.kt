package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel
import com.ddangddangddang.data.model.response.AuctionPreviewsResponse

object AuctionHomeModelMapper : Mapper<List<AuctionHomeModel>, AuctionPreviewsResponse> {
    override fun AuctionPreviewsResponse.toPresentation(): List<AuctionHomeModel> {
        return auctions.map {
            AuctionHomeModel(
                it.id,
                it.title,
                it.image,
                it.auctionPrice,
                AuctionHomeStatusModel.find(it.status),
                it.auctioneerCount,
            )
        }
    }
}
