package com.ddang.ddang.report.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAuctionReportRequest(
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        @Positive(message = "경매 아이디는 양수여야 합니다.")
        Long auctionId,

        @NotEmpty(message = "신고 내용이 입력되지 않았습니다.")
        String description) {
}
