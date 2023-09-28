package com.ddang.ddang.image.presentation.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public enum ImageRelativeUrl {

    AUCTION("/auctions/images/"),
    USER("/users/images/");

    private final String value;

    ImageRelativeUrl(final String value) {
        this.value = value;
    }

    public String getAbsoluteUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .build()
                                          .toUriString()
                                          .concat(value);
    }
}
