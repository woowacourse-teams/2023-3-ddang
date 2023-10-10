package com.ddang.ddang.user.presentation.util;

public class NameProcessor {

    private static final String UNKNOWN_NAME = "알 수 없음";

    private NameProcessor() {
    }

    public static String process(final boolean isDeleted, final String name) {
        if (isDeleted) {
            return UNKNOWN_NAME;
        }

        return name;
    }
}
