package com.ddang.ddang.bid.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBidRequest(
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        @Positive(message = "경매 아이디는 양수입니다.")
        Long auctionId,

        @NotNull(message = "입찰 금액이 입력되지 않았습니다.")
        @Positive(message = "입찰 금액은 양수입니다.")
        Integer bidPrice
) {
}
