package com.ddangddangddang.android.model

import java.time.LocalDateTime

data class BidHistoryModel(
    val name: String,
    val profileImage: String,
    val price: Int,
    val bidDateTime: LocalDateTime,
)
