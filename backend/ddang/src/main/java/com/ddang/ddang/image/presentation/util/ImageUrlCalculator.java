package com.ddang.ddang.image.presentation.util;

public class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculate(final ImageBaseUrl imageBaseUrl, final Long id) {
        final String baseUrl = imageBaseUrl.getBaseUrl();

        return baseUrl.concat(String.valueOf(id));
    }
}
