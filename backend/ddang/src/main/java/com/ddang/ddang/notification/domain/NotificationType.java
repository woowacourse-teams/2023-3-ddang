package com.ddang.ddang.notification.domain;

public enum NotificationType {

    MESSAGE("message"),
    BID("bid"),
    ;

    private final String value;

    NotificationType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
