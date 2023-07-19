package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import com.ddangddangddang.android.R

enum class AuctionDetailStatusModel(
    val priceStatus: String,
    val progressStatus: String,
    @ColorRes val colorId: Int,
) {
    ONGOING("현재가", "경매중", R.color.red_100),
    UNBIDDEN("입찰전", "경매중", R.color.green),
    SUCCESS("낙찰가", "낙찰 완료", R.color.black_600),
    FAILURE("입찰전", "경매 유찰", R.color.black_600),
    ;

    companion object {
        private const val ERROR_NOT_FOUND = "[ERROR] 매칭되는 Status가 존재하지 않습니다"
        fun findStatus(status: String): AuctionDetailStatusModel {
            return when (status) {
                "ongoing" -> ONGOING
                "unbidden" -> UNBIDDEN
                "success" -> SUCCESS
                "failure" -> FAILURE
                else -> throw IllegalArgumentException(ERROR_NOT_FOUND)
            }
        }
    }
}
