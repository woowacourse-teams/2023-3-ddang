package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.configuration.ImageRelativeUrlConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ImageRelativeUrlFinder {

    private final ImageRelativeUrlConfigurationProperties imageRelativeUrl;

    public String find(final ImageTargetType imageTargetType) {
        final String imageBaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                               .build()
                                                               .toUriString();

        if (ImageTargetType.AUCTION_IMAGE == imageTargetType) {
            return imageBaseUrl + imageRelativeUrl.auctionImage();
        }

        return imageBaseUrl + imageRelativeUrl.profileImage();
    }
}
