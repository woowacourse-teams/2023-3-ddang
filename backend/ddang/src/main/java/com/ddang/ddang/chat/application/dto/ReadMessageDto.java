package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;

import java.time.LocalDateTime;

public record ReadMessageDto(
        Long id,
        LocalDateTime createdTime,
        Long chatRoomId,
        Long writerId,
        Long receiverId,
        String contents
) {

    public static ReadMessageDto of(
            final Message message,
            final ChatRoom chatRoom
    ) {
        return new ReadMessageDto(
                message.getId(),
                message.getCreatedTime(),
                chatRoom.getId(),
                message.getWriter().getId(),
                message.getReceiver().getId(),
                message.getContents()
        );
    }
}
