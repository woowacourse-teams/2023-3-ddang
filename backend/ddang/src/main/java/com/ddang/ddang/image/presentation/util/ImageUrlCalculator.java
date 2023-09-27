package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.application.util.ImageIdProcessor;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;

public final class ImageUrlCalculator {

    private ImageUrlCalculator() {
    }

    public static String calculate(final ImageBaseUrl imageBaseUrl, final Long id) {
        if (id == null) {
            return null;
        }

        // TODO: 2023/09/27 이름 변경
        final String baseUrl = imageBaseUrl.getBaseUrl();

        return baseUrl.concat(String.valueOf(id));
    }

    public static String calculateProfileImageUrl(final ProfileImage profileImage, final String baseUrl) {
        final Long profileImageId = ImageIdProcessor.process(profileImage);

        return baseUrl.concat(String.valueOf(profileImageId));
    }

    public static String calculateAuctionImageUrl(final AuctionImage auctionImage, final String baseUrl) {
        final Long auctionImageId = ImageIdProcessor.process(auctionImage);

        return baseUrl.concat(String.valueOf(auctionImageId));
    }
}
