package com.ddang.ddang.notification.application.dto;

public record CreateNotificationDto(
        Long targetUserId,
        String title,
        String body,
        String redirectUrl,
        String image
) {
}
