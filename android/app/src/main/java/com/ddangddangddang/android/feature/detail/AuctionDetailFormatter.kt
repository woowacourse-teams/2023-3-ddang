package com.ddangddangddang.android.feature.detail

import android.content.Context
import com.ddangddangddang.android.R
import com.ddangddangddang.android.model.AuctionDetailModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AuctionDetailFormatter {
    private const val emptyContent = ""
    fun formatClosingTime(dateTime: LocalDateTime?): String {
        if (dateTime == null) return emptyContent
        val formatter = DateTimeFormatter.ofPattern("'~ 'yyyy.MM.dd a hh:mm")
        return dateTime.format(formatter)
    }

    fun formatCategoryText(context: Context, mainCategory: String?, subCategory: String?): String {
        if (mainCategory.isNullOrBlank() || subCategory.isNullOrBlank()) return emptyContent
        return context.getString(R.string.detail_auction_category, mainCategory, subCategory)
    }

    fun formatAuctionStatusColor(context: Context, colorId: Int): Int {
        if (colorId == 0) return context.getColor(R.color.white)
        return context.getColor(colorId)
    }

    fun formatAuctionDetailStatus(
        context: Context,
        auctionDetailModel: AuctionDetailModel?,
    ): String {
        if (auctionDetailModel == null) return emptyContent
        val priceStatus = auctionDetailModel.auctionDetailStatusModel.priceStatus
        val lastBidPrice = auctionDetailModel.lastBidPrice
        return if (priceStatus == context.getString(R.string.all_auction_unbidden)) {
            priceStatus
        } else {
            context.getString(R.string.detail_auction_price_status, priceStatus, lastBidPrice)
        }
    }

    fun formatClosingRemainDateText(context: Context, remainTime: String?): String {
        if (remainTime == null) return emptyContent
        if (remainTime.isEmpty()) return context.getString(R.string.detail_auction_remain_time_finish)
        return context.getString(R.string.detail_auction_remain_time, remainTime)
    }

    fun getAuctionBottomButtonText(
        context: Context,
        bottomButtonStatus: AuctionDetailBottomButtonStatus?,
    ): String {
        if (bottomButtonStatus == null) return emptyContent
        return context.getString(bottomButtonStatus.text)
    }
}
