package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInChatRoomDto(Long id, String name, Long profileImageId, double reliability, boolean isDeleted) {

    public static ReadUserInChatRoomDto from(final User user) {
        return new ReadUserInChatRoomDto(
                user.getId(),
                user.getName(),
                user.getProfileImage().getId(),
                user.getReliability(),
                user.isDeleted()
        );
    }
}
