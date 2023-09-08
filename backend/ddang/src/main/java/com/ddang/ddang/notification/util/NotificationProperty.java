package com.ddang.ddang.notification.util;

public enum NotificationProperty {

    IMAGE("image"),
    TITLE("title"),
    BODY("body"),
    REDIRECT_URL("redirectUrl"),
    ;

    private final String keyName;

    NotificationProperty(final String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
