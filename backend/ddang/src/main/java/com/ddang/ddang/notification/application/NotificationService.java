package com.ddang.ddang.notification.application;

import com.ddang.ddang.configuration.fcm.exception.FcmNotFoundException;
import com.ddang.ddang.device.application.exception.DeviceTokenNotFoundException;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationStatus;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface NotificationService {

    NotificationStatus send(final CreateNotificationDto createNotificationDto) throws FirebaseMessagingException, NullPointerException, DeviceTokenNotFoundException, FcmNotFoundException;
}
