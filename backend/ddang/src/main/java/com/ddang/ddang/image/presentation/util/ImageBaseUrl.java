package com.ddang.ddang.image.presentation.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public enum ImageBaseUrl {

    // TODO: 2023/09/27 absolute, base, relative, full로 네이밍 변경
    AUCTION("/auctions/images/"),
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
