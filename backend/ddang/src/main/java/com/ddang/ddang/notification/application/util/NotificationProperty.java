package com.ddang.ddang.notification.application.util;

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

    public String getKeyName() {
        return keyName;
    }
}
