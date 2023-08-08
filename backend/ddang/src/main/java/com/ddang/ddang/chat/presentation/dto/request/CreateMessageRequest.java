package com.ddang.ddang.chat.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record CreateMessageRequest(
        @NotNull(message = "메시지 수신자 아이디가 입력되지 않았습니다.")
        Long receiverId,

        @NotNull(message = "메시지 내용이 입력되지 않았습니다.")
        String contents,

        @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
        LocalDateTime createdTime
) {
}
