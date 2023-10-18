package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.domain.ProfileImage;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final String storeName) {
        final String absoluteUrl = imageRelativeUrl.calculateAbsoluteUrl();

        if (storeName == null && imageRelativeUrl == ImageRelativeUrl.USER) {
            return absoluteUrl + ProfileImage.DEFAULT_PROFILE_IMAGE_STORE_NAME;
        }

        return absoluteUrl + storeName;
    }

    public static String calculateBy(final String imageAbsoluteUrl, final String storeName) {
        if (storeName == null && imageAbsoluteUrl.contains(ImageRelativeUrl.USER.getValue())) {
            return imageAbsoluteUrl + ProfileImage.DEFAULT_PROFILE_IMAGE_STORE_NAME;
        }

        return imageAbsoluteUrl + storeName;
    }
}
