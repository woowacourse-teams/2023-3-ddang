package com.ddang.ddang.authentication.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.infrastructure.persistence.fixture.JpaBlackListTokenRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaBlackListTokenRepositoryTest extends JpaBlackListTokenRepositoryFixture {

    @Autowired
    JpaBlackListTokenRepository blackListTokenRepository;

    @Test
    void BlackListToken_엔티티를_저장한다() {
        // when
        final BlackListToken actual = blackListTokenRepository.save(만료할_토큰);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 블랙리스트로_등록된_토큰인지_확인할때_이미_블랙리스트로_등록된_토큰을_전달하면_참을_반환한다() {
        // when
        final boolean actual = blackListTokenRepository
                .existsByTokenTypeAndToken(TokenType.ACCESS, 만료_토큰_내용);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 블랙리스트로_등록된_토큰인지_확인할때_블랙리스트로_등록되지_않은_토큰을_전달하면_거짓을_반환한다() {
        // when
        final boolean actual = blackListTokenRepository
                .existsByTokenTypeAndToken(TokenType.ACCESS, 만료되지_않은_토큰_내용);

        // then
        assertThat(actual).isFalse();
    }
}
