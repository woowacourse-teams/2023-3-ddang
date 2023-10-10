package com.ddang.ddang.authentication.configuration;

import com.ddang.ddang.authentication.domain.TokenType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("token.jwt")
public record JwtConfigurationProperties(
        String accessKey,
        String refreshKey,
        Long accessExpiredHours,
        Long refreshExpiredHours
) {

    public String findTokenKey(final TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessKey;
        }

        return refreshKey;
    }

    public Long findExpiredHours(final TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessExpiredHours;
        }

        return refreshExpiredHours;
    }
}
