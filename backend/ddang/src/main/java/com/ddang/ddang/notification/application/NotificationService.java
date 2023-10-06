package com.ddang.ddang.notification.application;

import com.ddang.ddang.notification.application.dto.CreateNotificationDto;

public interface NotificationService {

    String send(final CreateNotificationDto createNotificationDto);
}
