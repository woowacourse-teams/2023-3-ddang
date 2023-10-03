package com.ddang.ddang.questionandanswer.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAnswerRequest(
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        @Positive(message = "경매 아이디는 양수입니다.")
        Long auctionId,

        @NotEmpty(message = "답변이 입력되지 않았습니다.")
        String content
) {
}
