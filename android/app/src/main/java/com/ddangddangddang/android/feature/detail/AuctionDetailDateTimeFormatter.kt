package com.ddangddangddang.android.feature.detail

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AuctionDetailDateTimeFormatter {
    fun convertToClosingTimeFormatter(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("'~ 'yyyy.MM.dd a hh:mm")
        return dateTime.format(formatter)
    }
}
