package com.ddang.ddang.notification.application.dto;

import com.ddang.ddang.notification.domain.NotificationType;

public record CreateNotificationDto(
        NotificationType notificationType,
        Long targetUserId,
        String title,
        String body,
        String redirectUrl,
        String image
) {
}
