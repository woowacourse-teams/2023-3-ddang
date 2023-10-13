package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.domain.ProfileImage;

import java.util.Objects;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final Long id) {
        final String absoluteUrl = imageRelativeUrl.calculateAbsoluteUrl();

        return absoluteUrl + Objects.requireNonNullElse(id, ProfileImage.DEFAULT_PROFILE_IMAGE_ID);
    }

    public static String calculateBy(final String imageAbsoluteUrl, final Long id) {
        return imageAbsoluteUrl + id;
    }
}
