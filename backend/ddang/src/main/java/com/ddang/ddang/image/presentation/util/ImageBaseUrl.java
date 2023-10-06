package com.ddang.ddang.image.presentation.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public enum ImageBaseUrl {

    AUCTION("/auctions/images"),
    USER("/users/images/");

    private final String value;

    ImageBaseUrl(final String value) {
        this.value = value;
    }

    public String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .build()
                                          .toUriString()
                                          .concat(value);
    }
}
