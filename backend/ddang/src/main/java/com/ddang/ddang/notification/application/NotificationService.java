package com.ddang.ddang.notification.application;

import com.ddang.ddang.event.domain.SendNotificationEvent;
import com.ddang.ddang.notification.domain.NotificationStatus;

public interface NotificationService {

    NotificationStatus send(final SendNotificationEvent sendNotificationEvent);
}
