package com.ddang.ddang.image.application.util;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;

public final class ImageStoreNameProcessor {

    private ImageStoreNameProcessor() {
    }

    public static String process(final ProfileImage profileImage) {
        if (profileImage == null) {
            return null;
        }

        return profileImage.getImage().getStoreName();
    }

    public static String process(final AuctionImage auctionImage) {
        if (auctionImage == null) {
            return null;
        }

        return auctionImage.getImage().getStoreName();
    }
}
