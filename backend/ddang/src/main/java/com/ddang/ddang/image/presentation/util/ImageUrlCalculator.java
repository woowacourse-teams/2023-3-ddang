package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.application.util.ImageIdProcessor;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculate(final ImageRelativeUrl imageRelativeUrl, final Long id) {
        if (id == null) {
            return null;
        }

        final String absoluteUrl = imageRelativeUrl.getAbsoluteUrl();

        return absoluteUrl.concat(String.valueOf(id));
    }

    public static String calculateProfileImageUrl(final ProfileImage profileImage, final String absoluteUrl) {
        final Long profileImageId = ImageIdProcessor.process(profileImage);

        return absoluteUrl.concat(String.valueOf(profileImageId));
    }

    public static String calculateAuctionImageUrl(final AuctionImage auctionImage, final String absoluteUrl) {
        final Long auctionImageId = ImageIdProcessor.process(auctionImage);

        return absoluteUrl.concat(String.valueOf(auctionImageId));
    }
}
