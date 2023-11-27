package com.ddang.ddang.chat.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateMessageRequest(
        @NotNull(message = "메시지 수신자 아이디가 입력되지 않았습니다.")
        @Positive(message = "수신자 아이디는 양수입니다.")
        Long receiverId,

        @NotNull(message = "메시지 내용이 입력되지 않았습니다.")
        String content
) {
}
