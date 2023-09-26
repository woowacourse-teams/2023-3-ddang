package com.ddang.ddang.authentication.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.authentication.configuration.JwtConfigurationProperties;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtDecoderTest {

    JwtDecoder jwtDecoder;

    JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        final JwtConfigurationProperties jwtConfigurationProperties = new JwtConfigurationProperties(
                "thisistoolargeaccesstokenkeyfordummykeydataforlocal",
                "thisistoolargerefreshtokenkeyfordummykeydataforlocal",
                12L,
                1460L
        );

        jwtDecoder = new JwtDecoder(jwtConfigurationProperties);
        jwtEncoder = new JwtEncoder(jwtConfigurationProperties);
    }

    @Test
    void 토큰의_길이가_유효하지_않다면_예외가_발생한다() {
        // given
        final String invalidLengthToken = "abcde";

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(TokenType.ACCESS, invalidLengthToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입이 아니거나 유효한 토큰이 아닙니다.");
    }

    @Test
    void 토큰의_타입이_유효하지_않다면_예외가_발생한다() {
        // given
        final String invalidTypeToken = "Basic12 abcde";

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(TokenType.ACCESS, invalidTypeToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입이 아닙니다.");
    }

    @Test
    void 토큰이_유효하지_않다면_빈_Optional을_반환한다() {
        // given
        final String invalidToken = "Bearer adf";

        // when
        final Optional<PrivateClaims> actual = jwtDecoder.decode(TokenType.ACCESS, invalidToken);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 유효한_토큰이면_해당_토큰의_본문을_반환한다() {
        // given
        final Map<String, Object> privateClaims = Map.of("userId", 1L);
        final String accessToken = jwtEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, privateClaims);

        // when
        final Optional<PrivateClaims> actual = jwtDecoder.decode(TokenType.ACCESS, accessToken);

        // then
        assertThat(actual).isPresent();
    }
}
