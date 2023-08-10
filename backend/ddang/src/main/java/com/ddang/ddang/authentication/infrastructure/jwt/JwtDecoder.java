package com.ddang.ddang.authentication.infrastructure.jwt;

import com.ddang.ddang.authentication.configuration.JwtConfigurationProperties;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder implements TokenDecoder {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final String CLAIM_NAME = "userId";
    private static final int BEARER_END_INDEX = 7;

    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    public Optional<PrivateClaims> decode(final TokenType tokenType, final String token) {
        validateBearerToken(token);

        return this.parse(tokenType, token)
                   .map(this::convert);
    }

    private void validateBearerToken(final String token) {
        try {
            final String tokenType = token.substring(0, BEARER_END_INDEX);

            validateTokenType(tokenType);
        } catch (final StringIndexOutOfBoundsException ex) {
            throw new InvalidTokenException("Bearer 타입이 아니거나 유효한 토큰이 아닙니다.", ex);
        }
    }

    private void validateTokenType(final String tokenType) {
        if (!BEARER_TOKEN_PREFIX.equals(tokenType)) {
            throw new InvalidTokenException("Bearer 타입이 아닙니다.");
        }
    }

    private Optional<Claims> parse(final TokenType tokenType, final String token) {
        final String key = jwtConfigurationProperties.findTokenKey(tokenType);

        try {
            return Optional.of(
                    Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(findPureToken(token))
                        .getBody()
            );
        } catch (final JwtException ignored) {
            return Optional.empty();
        }
    }

    private PrivateClaims convert(final Claims claims) {
        return new PrivateClaims(claims.get(CLAIM_NAME, Long.class));
    }

    private String findPureToken(final String token) {
        return token.substring(BEARER_TOKEN_PREFIX.length());
    }
}
