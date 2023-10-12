package com.ddangddangddang.android.feature.detail

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.ddangddangddang.android.R
import com.ddangddangddang.android.feature.detail.bidhistory.BidHistoryFragment
import com.ddangddangddang.android.feature.detail.info.AuctionInfoFragment
import com.ddangddangddang.android.feature.detail.qna.QnaFragment

enum class DetailFragmentType(val tag: String, @StringRes val nameId: Int) {
    AUCTION_INFO("auction_info", R.string.detail_auction_info_title) {
        override fun create(): Fragment {
            return AuctionInfoFragment()
        }
    },
    QNA("qna_tag", R.string.detail_auction_qna_title) {
        override fun create(): Fragment {
            return QnaFragment()
        }
    },
    BID_HISTORY("bid_history_tag", R.string.detail_auction_bid_history) {
        override fun create(): Fragment {
            return BidHistoryFragment()
        }
    },
    ;

    abstract fun create(): Fragment

    companion object {
        fun getTypeFrom(position: Int): DetailFragmentType {
            return values()[position]
        }
    }
}
