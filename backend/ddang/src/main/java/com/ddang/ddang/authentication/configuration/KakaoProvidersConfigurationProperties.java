package com.ddang.ddang.authentication.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth2.client.providers.kakao")
public record KakaoProvidersConfigurationProperties(String adminKey, String userInfoUri, String userUnlinkUri) {
}
