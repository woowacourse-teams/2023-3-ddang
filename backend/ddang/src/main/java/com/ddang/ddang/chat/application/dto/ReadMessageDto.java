package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.user.domain.User;

public record ReadMessageDto(Long id, ChatRoom chatRoom, User writer, User receiver, String contents) {

    public static ReadMessageDto from(final Message message) {
        return new ReadMessageDto(
                message.getId(),
                message.getChatRoom(),
                message.getWriter(),
                message.getReceiver(),
                message.getContents()
        );
    }
}
