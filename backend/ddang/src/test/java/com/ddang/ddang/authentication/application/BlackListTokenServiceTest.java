package com.ddang.ddang.authentication.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.exception.EmptyTokenException;
import com.ddang.ddang.authentication.infrastructure.persistence.JpaBlackListTokenRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlackListTokenServiceTest {

    @Autowired
    BlackListTokenService blackListTokenService;

    @Autowired
    JpaBlackListTokenRepository blackListTokenRepository;

    @Autowired
    TokenEncoder tokenEncoder;

    @Test
    void 유효한_accessToken과_refreshToken을_전달하면_블랙리스트로_등록한다() {
        // given
        final Map<String, Object> privateClaims = Map.of("userId", 1L);
        final String accessToken =
                "Bearer " + tokenEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, privateClaims);
        final String refreshToken =
                "Bearer " + tokenEncoder.encode(LocalDateTime.now(), TokenType.REFRESH, privateClaims);

        // when & then
        assertDoesNotThrow(() -> blackListTokenService.registerBlackListToken(accessToken, refreshToken));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'':Bearer refreshToken", ":Bearer refreshToken", "'   ':Bearer refreshToken",
            "Bearer accessToken:''", "Bearer accessToken:", "Bearer accessToken:'     '"
    }, delimiter = ':')
    void 빈_토큰을_블랙리스트에_등록하면_예외가_발생한다(final String accessToken, final String refreshToken) {
        // when & then
        assertThatThrownBy(() -> blackListTokenService.registerBlackListToken(accessToken, refreshToken))
                .isInstanceOf(EmptyTokenException.class)
                .hasMessage("비어있는 토큰입니다.");
    }

    @Test
    void 블랙리스트로_등록된_토큰인지_확인할때_이미_블랙리스트로_등록된_토큰을_전달하면_참을_반환한다() {
        // given
        final Map<String, Object> privateClaims = Map.of("userId", 1L);
        final String accessToken =
                "Bearer " + tokenEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, privateClaims);
        final String refreshToken =
                "Bearer " + tokenEncoder.encode(LocalDateTime.now(), TokenType.REFRESH, privateClaims);

        blackListTokenService.registerBlackListToken(accessToken, refreshToken);

        // when
        final boolean actual = blackListTokenService.existsBlackListToken(TokenType.ACCESS, accessToken);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 블랙리스트로_등록된_토큰인지_확인할때_블랙리스트로_등록되지_않은_토큰을_전달하면_거짓을_반환한다() {
        // given
        final String invalidAccessToken = "invalidAccessToken";

        // when
        final boolean actual = blackListTokenService.existsBlackListToken(TokenType.ACCESS, invalidAccessToken);

        // then
        assertThat(actual).isFalse();
    }
}
