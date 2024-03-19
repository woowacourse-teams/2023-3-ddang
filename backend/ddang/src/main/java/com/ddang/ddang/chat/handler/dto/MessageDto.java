package com.ddang.ddang.chat.handler.dto;

import com.ddang.ddang.chat.domain.Message;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,

        boolean isMyMessage,

        String contents
) {

    public static MessageDto of(final Message message, final boolean isMyMessage) {
        return new MessageDto(
                message.getId(),
                message.getCreatedTime(),
                isMyMessage,
                message.getContents()
        );
    }
}
