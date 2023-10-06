package com.ddang.ddang.authentication.infrastructure.jwt;

import com.ddang.ddang.authentication.configuration.JwtConfigurationProperties;
import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtEncoder implements TokenEncoder {

    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    public String encode(
            final LocalDateTime publishTime,
            final TokenType tokenType,
            final Map<String, Object> privateClaims
    ) {
        final Date targetDate = convertDate(publishTime);
        final String key = jwtConfigurationProperties.findTokenKey(tokenType);
        final Long expiredHours = jwtConfigurationProperties.findExpiredHours(tokenType);

        return Jwts.builder()
                   .setIssuedAt(targetDate)
                   .setExpiration(new Date(targetDate.getTime() + expiredHours * 60 * 60 * 1000L))
                   .addClaims(privateClaims)
                   .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                   .compact();
    }

    private Date convertDate(final LocalDateTime targetTime) {
        final Instant targetInstant = targetTime.atZone(ZoneId.of("Asia/Seoul"))
                                                .toInstant();

        return Date.from(targetInstant);
    }
}
