package com.ddangddangddang.android.feature.detail

import android.content.Context
import com.ddangddangddang.android.model.AuctionDetailModel
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AuctionDetailFormatter {
    private const val emptyContent = ""
    fun formatClosingTime(dateTime: LocalDateTime?): String {
        if (dateTime == null) return emptyContent
        val formatter = DateTimeFormatter.ofPattern("'~ 'yyyy.MM.dd a hh:mm")
        return dateTime.format(formatter)
    }

    fun formatAuctionDetailStatus(auctionDetailModel: AuctionDetailModel?): String {
        if (auctionDetailModel == null) return emptyContent
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

    fun getAuctionBottomButtonText(
        context: Context,
        bottomButtonStatus: AuctionDetailBottomButtonStatus?,
    ): String {
        return bottomButtonStatus?.let {
            context.getString(it.text)
        } ?: emptyContent
    }
}
