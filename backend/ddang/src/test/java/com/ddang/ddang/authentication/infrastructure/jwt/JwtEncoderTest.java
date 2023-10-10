package com.ddang.ddang.authentication.infrastructure.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.infrastructure.jwt.fixture.JwtEncoderFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtEncoderTest extends JwtEncoderFixture {

    @Test
    void 토큰을_생성한다() {
        // given
        final JwtEncoder jwtEncoder = new JwtEncoder(토큰_설정);

        // when
        final String actual = jwtEncoder.encode(LocalDateTime.now(), TokenType.ACCESS, 토큰_내용);

        // then
        assertThat(actual).isNotBlank()
                          .contains("Bearer ");
    }
}
