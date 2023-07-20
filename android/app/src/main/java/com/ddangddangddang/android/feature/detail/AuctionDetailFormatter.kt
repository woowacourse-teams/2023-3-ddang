package com.ddangddangddang.android.feature.detail

import com.ddangddangddang.android.model.AuctionDetailModel
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AuctionDetailFormatter {
    fun formatClosingTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("'~ 'yyyy.MM.dd a hh:mm")
        return dateTime.format(formatter)
    }

    fun formatAuctionDetailStatus(auctionDetailModel: AuctionDetailModel): String {
        val priceStatus = auctionDetailModel.auctionDetailStatusModel.priceStatus
        val progressStatus = auctionDetailModel.auctionDetailStatusModel.progressStatus
        val lastBidPrice = auctionDetailModel.lastBidPrice
        val formatter = DecimalFormat("#,###")
        return if (priceStatus == "입찰전") {
            priceStatus
        } else {
            "$priceStatus ${formatter.format(lastBidPrice)}원"
        }
    }
}
