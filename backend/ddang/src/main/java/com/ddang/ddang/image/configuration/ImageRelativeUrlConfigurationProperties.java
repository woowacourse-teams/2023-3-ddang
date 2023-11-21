package com.ddang.ddang.image.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("image.url")
public record ImageRelativeUrlConfigurationProperties(String auctionImage, String profileImage) {
}
