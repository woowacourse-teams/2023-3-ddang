package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.image.application.util.ImageIdProcessor;
import com.ddang.ddang.user.domain.User;

public record ReadUserInChatRoomDto(Long id, String name, Long profileImageId, Double reliability, boolean isDeleted) {

    public static ReadUserInChatRoomDto from(final User user) {
        return new ReadUserInChatRoomDto(
                user.getId(),
                user.getName(),
                ImageIdProcessor.process(user.getProfileImage()),
                user.getReliability(),
                user.isDeleted()
        );
    }
}
