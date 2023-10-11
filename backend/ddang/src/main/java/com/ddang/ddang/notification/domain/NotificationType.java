package com.ddang.ddang.notification.domain;

import lombok.Getter;

@Getter
public enum NotificationType {

    MESSAGE("message"),
    BID("bid"),
    QUESTION("question"),
    ANSWER("answer");

    private final String value;

    NotificationType(final String value) {
        this.value = value;
    }
}
