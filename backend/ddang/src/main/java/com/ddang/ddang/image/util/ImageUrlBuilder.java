package com.ddang.ddang.image.util;

// TODO: 2023/09/08 [고민] 이미지 util의 패키지 위치는?
public class ImageUrlBuilder {

    private ImageUrlBuilder() {
    }

    public static String calculate(final ImageBaseUrl imageBaseUrl, final Long id) {
        final String baseUrl = imageBaseUrl.getBaseUrl();

        return baseUrl.concat(String.valueOf(id));
    }
}
