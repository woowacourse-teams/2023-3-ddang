package com.ddang.ddang.image.presentation.util;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculate(final ImageBaseUrl imageBaseUrl, final Long id) {
        if (id == null) {
            return null;
        }

        final String baseUrl = imageBaseUrl.getBaseUrl();

        return baseUrl.concat(String.valueOf(id));
    }
}
