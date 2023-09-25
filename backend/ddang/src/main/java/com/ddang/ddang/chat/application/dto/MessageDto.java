package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,
        LocalDateTime createdTime,
        ChatRoom chatRoom,
        User writer,
        User receiver,
        String contents,
        String baseUrl
) {

    public static MessageDto from(
            final Message persistMessage,
            final ChatRoom chatRoom,
            final User writer,
            final User receiver,
            final String baseUrl
    ) {
        return new MessageDto(
                persistMessage.getId(),
                persistMessage.getCreatedTime(),
                chatRoom,
                writer,
                receiver,
                persistMessage.getContents(),
                baseUrl
        );
    }
}
