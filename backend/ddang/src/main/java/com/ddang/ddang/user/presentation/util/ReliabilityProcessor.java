package com.ddang.ddang.user.presentation.util;

public class ReliabilityProcessor {

    private static final Float EMPTY_RELIABILITY = null;

    private ReliabilityProcessor() {
    }

    public static Float process(final double reliability) {
        if (reliability < 0.0d) {
            return EMPTY_RELIABILITY;
        }

        return (float) reliability;
    }
}
