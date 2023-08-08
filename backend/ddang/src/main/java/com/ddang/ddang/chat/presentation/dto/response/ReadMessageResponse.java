package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadMessageDto;

import java.time.LocalDateTime;

public record ReadMessageResponse(Long id, LocalDateTime createdAt, boolean isMyMessage, String contents) {

    public static ReadMessageResponse of(final ReadMessageDto readMessageDto, final boolean isMyMessage) {
        return new ReadMessageResponse(
                readMessageDto.id(),
                readMessageDto.createdAt(),
                isMyMessage,
                readMessageDto.contents()
        );
    }
}
