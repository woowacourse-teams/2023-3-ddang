package com.ddang.ddang.notification.application.util;

// TODO: 2023/09/30 안드로이드분들께 image -> imageUrl로 변경 가능한지 여쭤보기
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
