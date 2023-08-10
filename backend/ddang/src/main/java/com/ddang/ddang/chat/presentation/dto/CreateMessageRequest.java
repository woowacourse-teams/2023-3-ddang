package com.ddang.ddang.chat.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record CreateMessageRequest(
        @NotNull(message = "경매 아이디가 입력되지 않았습니다.")
        Long auctionId,
        @NotNull(message = "채팅방 아이디가 입력되지 않았습니다.")
        Long chatRoomId,
        @NotNull(message = "메시지 발신자 아이디가 입력되지 않았습니다.")
        Long writerId,

        @NotNull(message = "메시지 수신자 아이디가 입력되지 않았습니다.")
        Long receiverId,

        @NotNull(message = "메시지 내용이 입력되지 않았습니다.")
        String contents
) {
}
