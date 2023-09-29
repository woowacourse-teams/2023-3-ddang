package com.ddang.ddang.notification.application.dto;

import com.ddang.ddang.chat.application.dto.MessageDto;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.notification.domain.NotificationType;
import lombok.NonNull;

public record CreateNotificationDto(
        @NonNull
        NotificationType notificationType,

        @NonNull
        Long targetUserId,

        @NonNull
        String title,

        @NonNull
        String body,

        @NonNull
        String redirectUrl,

        @NonNull
        String image
) {

    public static CreateNotificationDto from(final MessageDto messageDto) {
        return new CreateNotificationDto(
                NotificationType.MESSAGE,
                messageDto.receiver().getId(),
                messageDto.writer().getName(),
                messageDto.contents(),
                calculateRedirectUrl(messageDto.chatRoom().getId()),
                ImageUrlCalculator.calculateProfileImageUrl(messageDto.receiver().getProfileImage(), messageDto.baseUrl())
        );
    }

    private static String calculateRedirectUrl(final Long id) {
        return "/chattings/" + id;
    }
}
