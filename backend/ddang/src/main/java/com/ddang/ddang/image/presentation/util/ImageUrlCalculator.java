package com.ddang.ddang.image.presentation.util;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final Long id) {
        if (id == null) {
            return null;
        }

        final String absoluteUrl = imageRelativeUrl.getAbsoluteUrl();

        return absoluteUrl.concat(String.valueOf(id));
    }

    public static String calculateBy(final String imageAbsoluteUrl, final Long id) {
        if (id == null) {
            return null;
        }

        return imageAbsoluteUrl.concat(String.valueOf(id));
    }
}
