package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ddangddangddang.android.R

enum class AuctionHomeStatusModel(
    @StringRes val priceStatusId: Int,
    @StringRes val progressStatusId: Int,
    @ColorRes val colorId: Int,
) {
    ONGOING(R.string.all_current_price, R.string.all_auction_ongoing, R.color.red_900),
    UNBIDDEN(R.string.all_start_price, R.string.all_auction_ongoing, R.color.green),
    SUCCESS(R.string.all_winning_bid_price, R.string.all_auction_success, R.color.grey_700),
    FAILURE(R.string.all_start_price, R.string.all_auction_failure, R.color.grey_700),
    ;

    companion object {
        private const val ERROR_NOT_FOUND = "[ERROR] 매칭되는 Status가 존재하지 않습니다"
        fun find(status: String): AuctionHomeStatusModel {
            return values().find { it.name == status } ?: throw IllegalArgumentException(
                ERROR_NOT_FOUND,
            )
        }
    }
}
