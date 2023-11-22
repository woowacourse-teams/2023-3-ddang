package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInChatRoomDto(
        Long id,
        String name,
        String profileImageStoreName,
        double reliability,
        boolean isDeleted
) {

    public static ReadUserInChatRoomDto from(final User user) {
        return new ReadUserInChatRoomDto(
                user.getId(),
                user.findName(),
                user.getProfileImageStoreName(),
                user.getReliability().getValue(),
                user.isDeleted()
        );
    }
}
