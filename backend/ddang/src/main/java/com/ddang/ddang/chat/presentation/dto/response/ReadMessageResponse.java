package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadMessageResponse(
        Long id,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,

        boolean isMyMessage,

        String contents
) {

    public static ReadMessageResponse of(final ReadMessageDto readMessageDto, final boolean isMyMessage) {
        return new ReadMessageResponse(
                readMessageDto.id(),
                readMessageDto.createdTime(),
                isMyMessage,
                readMessageDto.contents()
        );
    }
}
