package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadLastMessageDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadLastMessageResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,

        String contents
) {

    public static ReadLastMessageResponse from(final ReadLastMessageDto dto) {
        return new ReadLastMessageResponse(dto.createdTime(), dto.contents());
    }
}
