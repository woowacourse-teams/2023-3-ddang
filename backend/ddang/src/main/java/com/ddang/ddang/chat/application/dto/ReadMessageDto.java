package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadMessageDto(
        Long id,
        LocalDateTime createdAt,
        ChatRoom chatRoom,
        User writer,
        User receiver,
        String contents
) {

    public static ReadMessageDto from(final Message message) {
        return new ReadMessageDto(
                message.getId(),
                message.getCreatedTime(),
                message.getChatRoom(),
                message.getWriter(),
                message.getReceiver(),
                message.getContents()
        );
    }
}
