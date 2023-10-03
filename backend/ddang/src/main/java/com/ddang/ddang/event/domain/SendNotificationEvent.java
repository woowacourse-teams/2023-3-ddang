package com.ddang.ddang.event.domain;

import com.ddang.ddang.notification.application.dto.CreateNotificationDto;

public record SendNotificationEvent(CreateNotificationDto createNotificationDto) {
}
