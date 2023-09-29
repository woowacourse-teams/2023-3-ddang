package com.ddang.ddang.image.presentation.util;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final long id) {
        final String absoluteUrl = imageRelativeUrl.calculateAbsoluteUrl();

        return absoluteUrl + id;
    }

    public static String calculateBy(final String imageAbsoluteUrl, final long id) {
        return imageAbsoluteUrl + id;
    }
}
