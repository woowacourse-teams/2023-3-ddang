package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ddangddangddang.android.R

enum class AuctionHomeStatusModel(
    @StringRes val priceStatus: Int,
    @StringRes val progressStatus: Int,
    @ColorRes val colorId: Int,
) {
    ONGOING(R.string.all_current_price, R.string.all_auction_ongoing, R.color.red_100),
    UNBIDDEN(R.string.all_start_price, R.string.all_auction_ongoing, R.color.green),
    SUCCESS(R.string.all_winning_bid_price, R.string.all_auction_success, R.color.black_600),
    FAILURE(R.string.all_start_price, R.string.all_auction_failure, R.color.black_600),
}
