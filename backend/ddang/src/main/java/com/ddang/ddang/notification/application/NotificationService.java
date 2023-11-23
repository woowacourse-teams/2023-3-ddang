package com.ddang.ddang.notification.application;

import com.ddang.ddang.notification.application.dto.request.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationStatus;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface NotificationService {

    NotificationStatus send(final CreateNotificationDto createNotificationDto) throws FirebaseMessagingException;
}
