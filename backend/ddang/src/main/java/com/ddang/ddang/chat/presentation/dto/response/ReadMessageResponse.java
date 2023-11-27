package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.response.ReadMessageDto;

import java.time.LocalDateTime;

public record ReadMessageResponse(
        Long id,
        LocalDateTime createdTime,
        boolean isMyMessage,
        String content
) {

    public static ReadMessageResponse of(final ReadMessageDto readMessageDto, final boolean isMyMessage) {
        return new ReadMessageResponse(
                readMessageDto.id(),
                readMessageDto.createdTime(),
                isMyMessage,
                readMessageDto.content()
        );
    }
}
