package com.ddang.ddang.authentication.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.infrastructure.jwt.fixture.JwtDecoderFixture;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtDecoderTest extends JwtDecoderFixture {

    @Test
    void 토큰의_길이가_유효하지_않다면_예외가_발생한다() {
        // given
        final JwtDecoder jwtDecoder = new JwtDecoder(토큰_설정);

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(TokenType.ACCESS, 유효하지_않은_길이의_토큰))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입이 아니거나 유효한 토큰이 아닙니다.");
    }

    @Test
    void 토큰의_타입이_유효하지_않다면_예외가_발생한다() {
        // given
        final JwtDecoder jwtDecoder = new JwtDecoder(토큰_설정);

        // when & then
        assertThatThrownBy(() -> jwtDecoder.decode(TokenType.ACCESS, 유효하지_않은_타입의_토큰))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입이 아닙니다.");
    }

    @Test
    void 토큰이_유효하지_않다면_빈_Optional을_반환한다() {
        // given
        final JwtDecoder jwtDecoder = new JwtDecoder(토큰_설정);

        // when
        final Optional<PrivateClaims> actual = jwtDecoder.decode(TokenType.ACCESS, 유효하지_않은_토큰);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 유효한_토큰이면_해당_토큰의_본문을_반환한다() {
        // given
        final JwtDecoder jwtDecoder = new JwtDecoder(토큰_설정);

        // when
        final Optional<PrivateClaims> actual = jwtDecoder.decode(TokenType.ACCESS, 유효한_토큰);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().userId()).isEqualTo(토큰_내용.get(회원_아이디_키));
        });
    }
}
