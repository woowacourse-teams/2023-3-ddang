package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.domain.ProfileImage;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final Long id) {
        final String absoluteUrl = imageRelativeUrl.calculateAbsoluteUrl();

        if (id == null && imageRelativeUrl == ImageRelativeUrl.USER) {
            return absoluteUrl + ProfileImage.DEFAULT_PROFILE_IMAGE_STORE_NAME;
        }

        return absoluteUrl + id;
    }

    public static String calculateBy(final String imageAbsoluteUrl, final Long id) {
        if (id == null && imageAbsoluteUrl.contains(ImageRelativeUrl.USER.getValue())) {
            return imageAbsoluteUrl + ProfileImage.DEFAULT_PROFILE_IMAGE_STORE_NAME;
        }

        return imageAbsoluteUrl + id;
    }
}
