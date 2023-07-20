package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ddangddangddang.android.R
import java.lang.IllegalArgumentException

enum class AuctionHomeStatusModel(
    @StringRes val priceStatusId: Int,
    @StringRes val progressStatusId: Int,
    @ColorRes val colorId: Int,
) {
    ONGOING(R.string.all_current_price, R.string.all_auction_ongoing, R.color.red_100),
    UNBIDDEN(R.string.all_start_price, R.string.all_auction_ongoing, R.color.green),
    SUCCESS(R.string.all_winning_bid_price, R.string.all_auction_success, R.color.black_600),
    FAILURE(R.string.all_start_price, R.string.all_auction_failure, R.color.black_600),
    ;

    companion object {
        fun find(status: String): AuctionHomeStatusModel {
            return values().find { it.name == status } ?: throw IllegalArgumentException()
        }
    }
}
