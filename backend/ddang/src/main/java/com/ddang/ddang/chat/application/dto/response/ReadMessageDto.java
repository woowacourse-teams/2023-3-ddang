package com.ddang.ddang.chat.application.dto.response;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;

import java.time.LocalDateTime;

public record ReadMessageDto(
        Long id,
        LocalDateTime createdTime,
        Long chatRoomId,
        Long writerId,
        Long receiverId,
        String content
) {

    public static ReadMessageDto from(
            final Message message,
            final ChatRoom chatRoom
    ) {
        return new ReadMessageDto(
                message.getId(),
                message.getCreatedTime(),
                chatRoom.getId(),
                message.getWriter().getId(),
                message.getReceiver().getId(),
                message.getContent()
        );
    }
}
