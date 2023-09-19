package com.ddangddangddang.android.feature.detail

import android.content.Context
import com.ddangddangddang.android.R
import com.ddangddangddang.android.model.AuctionDetailModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.GregorianCalendar

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

    fun formatClosingRemainDateText(context: Context, closingTime: LocalDateTime?): String {
        if (closingTime == null) return emptyContent
        val remainTime = closingTime.remainTimeFromNow()
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

    private fun LocalDateTime.remainTimeFromNow(): String {
        val nowCalendar = LocalDateTime.now().toCalendar()
        val nowDT = nowCalendar.time

        val closingCalendar = this.toCalendar()
        val closingDT = closingCalendar.time

        val differenceInMills = closingDT.time - nowDT.time
        if (differenceInMills <= 0) return ""
        if (differenceInMills < 1000L * 60) return "${(differenceInMills / (1000L)) % 60}초"

        val days = (differenceInMills / (24 * 60 * 60 * 1000L)) % 365
        val hours = (differenceInMills / (60 * 60 * 1000L)) % 24
        val minutes = (differenceInMills / (60 * 1000L)) % 60

        return buildString {
            if (days > 0L) append("${days}일")
            if (hours > 0L) append(" ${hours}시간")
            if (minutes > 0L) append(" ${minutes}분")
        }.trim()
    }

    private fun LocalDateTime.toCalendar(): GregorianCalendar {
        return GregorianCalendar(
            year,
            monthValue,
            dayOfMonth,
            hour,
            minute,
            second,
        )
    }
}
