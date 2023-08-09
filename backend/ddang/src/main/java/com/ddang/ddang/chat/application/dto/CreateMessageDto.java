package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.user.domain.User;

public record CreateMessageDto(
        Long chatRoomId,
        Long writerId,
        Long receiverId,
        String contents
) {

    public static CreateMessageDto of(final Long writerId, final Long chatRoomId, final CreateMessageRequest request) {
        return new CreateMessageDto(
                chatRoomId,
                writerId,
                request.receiverId(),
                request.contents()
        );
    }

    public Message toEntity(
            final ChatRoom chatRoom,
            final User writer,
            final User receiver
    ) {
        return Message.builder()
                      .chatRoom(chatRoom)
                      .writer(writer)
                      .receiver(receiver)
                      .contents(contents)
                      .build();
    }
}
