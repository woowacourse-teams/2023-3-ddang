package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ddangddangddang.android.R

enum class QnaStatusModel(
    @StringRes val questionStatus: Int,
    @ColorRes val colorId: Int,
) {
    WAITING(R.string.detail_auction_qna_waiting, R.color.grey_700),
    COMPLETE(R.string.detail_auction_qna_complete, R.color.red_300),
    ;

    companion object {
        fun find(isExistAnswer: Boolean): QnaStatusModel {
            return if (isExistAnswer) {
                COMPLETE
            } else {
                WAITING
            }
        }
    }
}
