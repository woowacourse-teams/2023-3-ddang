package com.ddang.ddang.auction.presentation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateAuctionRequest(
        @NotEmpty(message = "제목이 입력되지 않았습니다.")
        String title,

        @NotEmpty(message = "설명이 입력되지 않았습니다.")
        String description,

        @NotNull(message = "입찰 단위가 입력되지 않았습니다.")
        @Positive(message = "금액은 음수를 입력할 수 없습니다.")
        Integer bidUnit,

        @NotNull(message = "시작가가 입력되지 않았습니다.")
        @Positive(message = "금액은 음수를 입력할 수 없습니다.")
        Integer startBidPrice,

        @NotNull(message = "마감 시간이 입력되지 않았습니다.")
        @FutureOrPresent(message = "마감 시간은 과거를 입력할 수 없습니다.")
        LocalDateTime closingTime
) {
}
