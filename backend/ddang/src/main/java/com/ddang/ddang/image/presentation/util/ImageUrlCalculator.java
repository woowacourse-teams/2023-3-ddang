package com.ddang.ddang.image.presentation.util;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    // TODO: 9/29/23 id 타입을 long으로 변경 및 이미지는 null이 되는 경우가 없도록 할 것
    public static String calculateBy(final ImageRelativeUrl imageRelativeUrl, final Long id) {
        final String absoluteUrl = imageRelativeUrl.calculateAbsoluteUrl();

        return absoluteUrl + id;
    }

    public static String calculateBy(final String imageAbsoluteUrl, final Long id) {
        return imageAbsoluteUrl + id;
    }
}
