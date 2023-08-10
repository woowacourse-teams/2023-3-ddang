package com.ddang.ddang.chat.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateChatRoomRequest(
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        @Positive(message = "경매 아이디는 양수입니다.")
        Long auctionId
) {
}
