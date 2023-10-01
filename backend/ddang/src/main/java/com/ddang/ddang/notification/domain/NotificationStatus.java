package com.ddang.ddang.notification.domain;

import java.util.Objects;

public enum NotificationStatus {

    SUCCESS(true),
    FAIL(false);

    private final boolean isSuccess;

    NotificationStatus(final boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static NotificationStatus calculateStatus(final String messageId) {
        if (Objects.nonNull(messageId)) {
            return SUCCESS;
        }
        return FAIL;
    }
}
