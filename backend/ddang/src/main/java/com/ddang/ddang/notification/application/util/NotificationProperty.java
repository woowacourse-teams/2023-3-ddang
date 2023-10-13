package com.ddang.ddang.notification.application.util;

import lombok.Getter;

@Getter
public enum NotificationProperty {

    NOTIFICATION_TYPE("type"),
    IMAGE("image"),
    TITLE("title"),
    BODY("body"),
    REDIRECT_URL("redirectUrl");

    private final String keyName;

    NotificationProperty(final String keyName) {
        this.keyName = keyName;
    }
}
