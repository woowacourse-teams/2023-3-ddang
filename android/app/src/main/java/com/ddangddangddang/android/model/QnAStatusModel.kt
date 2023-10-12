package com.ddangddangddang.android.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ddangddangddang.android.R

enum class QnAStatusModel(
    @StringRes val questionStatus: Int,
    @ColorRes val colorId: Int,
) {
    WAITING(R.string.detail_auction_qna_waiting, R.color.grey_700),
    COMPLETE(R.string.detail_auction_qna_complete, R.color.red_300),
    ;

    companion object {
        private const val ERROR_NOT_FOUND = "[ERROR] 매칭되는 Status가 존재하지 않습니다"
        fun find(status: String): QnAStatusModel {
            return values().find { it.name == status } ?: throw IllegalArgumentException(
                ERROR_NOT_FOUND,
            )
        }
    }
}
