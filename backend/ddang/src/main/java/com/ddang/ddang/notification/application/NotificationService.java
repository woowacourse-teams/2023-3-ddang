package com.ddang.ddang.notification.application;

import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationStatus;

public interface NotificationService {

    NotificationStatus send(final CreateNotificationDto createNotificationDto);
}
