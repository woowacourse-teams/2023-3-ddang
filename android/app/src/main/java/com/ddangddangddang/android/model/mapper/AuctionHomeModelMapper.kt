package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel
import com.ddangddangddang.data.model.response.AuctionPreviewResponse

object AuctionHomeModelMapper : Mapper<AuctionHomeModel, AuctionPreviewResponse> {
    override fun AuctionPreviewResponse.toPresentation(): AuctionHomeModel {
        return AuctionHomeModel(
            id,
            title,
            image,
            auctionPrice,
            AuctionHomeStatusModel.find(status),
            auctioneerCount,
        )
    }
}
