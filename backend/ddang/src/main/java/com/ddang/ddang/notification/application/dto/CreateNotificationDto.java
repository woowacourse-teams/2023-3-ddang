package com.ddang.ddang.notification.application.dto;

import com.ddang.ddang.notification.domain.NotificationType;
import org.springframework.lang.NonNull;

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
}
