package com.ddang.ddang.image.presentation.util;

import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
public enum ImageRelativeUrl {

    AUCTION("/auctions/images/"),
    USER("/users/images/");

    private final String value;

    ImageRelativeUrl(final String value) {
        this.value = value;
    }

    public String calculateAbsoluteUrl() {
        final String imageBaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                               .build()
                                                               .toUriString();

        return imageBaseUrl + value;
    }
}
