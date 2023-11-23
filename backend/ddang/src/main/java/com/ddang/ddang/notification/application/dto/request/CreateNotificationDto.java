package com.ddang.ddang.notification.application.dto.request;

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
}
