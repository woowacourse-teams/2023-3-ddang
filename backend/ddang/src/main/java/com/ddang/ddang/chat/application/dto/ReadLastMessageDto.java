package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.Message;

import java.time.LocalDateTime;

public record ReadLastMessageDto(
        Long id,
        LocalDateTime createdTime,
        ReadUserInChatRoomDto writerDto,
        ReadUserInChatRoomDto receiverDto,
        String contents
) {
    public static ReadLastMessageDto from(final Message message) {
        return new ReadLastMessageDto(
                message.getId(),
                message.getCreatedTime(),
                ReadUserInChatRoomDto.from(message.getWriter()),
                ReadUserInChatRoomDto.from(message.getReceiver()),
                message.getContents()
        );
    }
}
