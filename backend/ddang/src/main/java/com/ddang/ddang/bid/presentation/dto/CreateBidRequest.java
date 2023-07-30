package com.ddang.ddang.bid.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record CreateBidRequest(
        // TODO: 2023/07/30  아이디에 대한 notnull 오류 메시지가 있어야 할까?
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        Long auctionId,

        @NotNull(message = "입찰 금액이 입력되지 않았습니다.")
        int bidPrice
) {
}
