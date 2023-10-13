package com.ddang.ddang.authentication.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.ddang.ddang.authentication.domain.exception.EmptyTokenException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BlackListTokenTest {

    @Test
    void 생성자에_유효한_토큰을_전달하면_BlackListToken을_반환한다() {
        // when & then
        assertDoesNotThrow(() -> new BlackListToken(TokenType.ACCESS, "accessToken"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"       ", ""})
    void 생성자에_비어_있는_토큰을_전달하면_예외가_발생한다(final String invalidToken) {
        // when & then
        assertThatThrownBy(() -> new BlackListToken(TokenType.ACCESS, invalidToken))
                .isInstanceOf(EmptyTokenException.class)
                .hasMessage("비어있는 토큰입니다.");
    }
}
