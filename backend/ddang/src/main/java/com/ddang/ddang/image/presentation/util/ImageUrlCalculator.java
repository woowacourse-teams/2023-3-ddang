package com.ddang.ddang.image.presentation.util;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final Long id) {
        final String absoluteUrl = imageRelativeUrl.getAbsoluteUrl();

        return absoluteUrl.concat(String.valueOf(id));
    }

    public static String calculateBy(final String imageAbsoluteUrl, final Long id) {
        return imageAbsoluteUrl.concat(String.valueOf(id));
    }
}
