package com.ddang.ddang.authentication.infrastructure.jwt;

import com.ddang.ddang.authentication.configuration.JwtConfigurationProperties;
import com.ddang.ddang.authentication.domain.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtEncoderTest {

    JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        final JwtConfigurationProperties jwtConfigurationProperties = new JwtConfigurationProperties(
                "thisistoolargeaccesstokenkeyfordummykeydataforlocal",
                "thisistoolargerefreshtokenkeyfordummykeydataforlocal",
                12L,
                1460L
        );
        jwtEncoder = new JwtEncoder(jwtConfigurationProperties);
    }

    @Test
    void 토큰을_생성한다() {
        // given
        final Map<String, Object> privateClaims = Map.of("userId", 1L);

        // when
        final String actual = jwtEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, privateClaims);

        // then
        assertThat(actual).isNotBlank();
    }
}
