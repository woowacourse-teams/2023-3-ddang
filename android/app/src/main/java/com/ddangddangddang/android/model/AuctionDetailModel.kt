package com.ddangddangddang.android.model

import java.time.LocalDateTime
import java.util.GregorianCalendar

data class AuctionDetailModel(
    val id: Long,
    val images: List<String>,
    val title: String,
    val mainCategory: String,
    val subCategory: String,
    val description: String,
    val startPrice: Int,
    val lastBidPrice: Int,
    val auctionDetailStatusModel: AuctionDetailStatusModel,
    val bidUnit: Int,
    val registerTime: LocalDateTime,
    val closingTime: LocalDateTime,
    val directRegions: List<RegionModel>,
    val auctioneerCount: Int,
    val sellerModel: SellerModel,
) {
    val remainTime: String
        get() {
            val registerCalendar = registerTime.toCalendar()
            val registerDT = registerCalendar.time

            val closingCalendar = closingTime.toCalendar()
            val closingDT = closingCalendar.time

            val differenceInMills = closingDT.time - registerDT.time

            val days = (differenceInMills / (24 * 60 * 60 * 1000L)) % 365
            val hours = (differenceInMills / (60 * 60 * 1000L)) % 24
            val minutes = (differenceInMills / (60 * 1000L)) % 60

            return buildString {
                if (days != 0L) append("${days}일")
                if (hours != 0L) append("${hours}시간")
                if (minutes != 0L) append("${minutes}분")
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
