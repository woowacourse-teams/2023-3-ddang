package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadMessageDto(
        Long id,
        LocalDateTime createdTime,
        ReadParticipatingChatRoomDto chatRoomDto,
        ReadUserDto writerDto,
        ReadUserDto receiverDto,
        String contents
) {

    public static ReadMessageDto from(final Message message) {
        return new ReadMessageDto(
                message.getId(),
                message.getCreatedTime(),
                toReadParticipatingChatRoomDto(message.getChatRoom(), message.getWriter()),
                ReadUserDto.from(message.getWriter()),
                ReadUserDto.from(message.getReceiver()),
                message.getContents()
        );
    }

    private static ReadParticipatingChatRoomDto toReadParticipatingChatRoomDto(
            final ChatRoom chatRoom,
            final User writer
    ) {
        return ReadParticipatingChatRoomDto.of(
                chatRoom.calculateChatPartnerOf(writer),
                chatRoom,
                chatRoom.getCreatedTime()
        );
    }
}
