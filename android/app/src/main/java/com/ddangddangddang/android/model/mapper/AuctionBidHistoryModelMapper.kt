package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.BidHistoryModel
import com.ddangddangddang.data.model.response.BidHistoryResponse
import java.time.LocalDateTime

object AuctionBidHistoryModelMapper : Mapper<BidHistoryModel, BidHistoryResponse> {
    override fun BidHistoryResponse.toPresentation(): BidHistoryModel {
        return BidHistoryModel(
            name,
            profileImage,
            price,
            LocalDateTime.parse(bidTime),
        )
    }
}
