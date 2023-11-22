package com.ddang.ddang.bid.application.event;

import com.ddang.ddang.bid.application.event.dto.NotificationPreviousBidDto;

public record BidNotificationEvent(NotificationPreviousBidDto notificationPreviousBidDto) {
}
