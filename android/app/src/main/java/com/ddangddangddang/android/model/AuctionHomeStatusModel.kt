package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import com.ddangddangddang.android.R

enum class AuctionHomeStatusModel(
    val priceStatus: String,
    val progressStatus: String,
    @ColorRes val colorId: Int,
) {
    ONGOING("현재가", "경매중", R.color.red_100),
    UNBIDDEN("시작가", "경매중", R.color.green),
    SUCCESS("낙찰가", "낙찰 완료", R.color.black_600),
    FAILURE("시작가", "경매 유찰", R.color.black_600),
}
