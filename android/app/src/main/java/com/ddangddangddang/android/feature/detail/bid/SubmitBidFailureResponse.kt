package com.ddangddangddang.android.feature.detail.bid

enum class SubmitBidFailureResponse(private val message: String) {
    FINISH("이미 종료된 경매입니다"),
    DELETED("삭제된 경매입니다"),
    UNDER_PRICE("마지막 입찰 금액보다 낮은 금액을 입력했습니다"),
    ALREADY_HIGHEST_BIDDER("이미 최고 입찰자입니다"),
    ELSE("기타"),
    ;

    companion object {
        fun find(message: String?): SubmitBidFailureResponse {
            return values().find { it.message == message } ?: ELSE
        }
    }
}
