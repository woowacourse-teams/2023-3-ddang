package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.annotations.QueryProjection;

public class ChatRoomWithLastMessageDto {
    private final ChatRoom chatRoom;
    private final Message message;

    @QueryProjection
    public ChatRoomWithLastMessageDto(final ChatRoom chatRoom, final Message message) {
        this.chatRoom = chatRoom;
        this.message = message;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public Message getMessage() {
        return message;
    }
}
