package com.ddang.ddang.bid.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBidRequest(
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        @Positive
        Long auctionId,

        @NotNull(message = "입찰 금액이 입력되지 않았습니다.")
        @Positive
        Integer bidPrice
) {
}
