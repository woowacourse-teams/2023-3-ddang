package com.ddang.ddang.authentication.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaBlackListTokenRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaBlackListTokenRepository blackListTokenRepository;

    @Test
    void BlackListToken_엔티티를_저장한다() {
        // given
        final BlackListToken blackListToken = new BlackListToken(TokenType.ACCESS, "accessToken");

        // when
        final BlackListToken actual = blackListTokenRepository.save(blackListToken);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 블랙리스트로_등록된_토큰인지_확인할때_이미_블랙리스트로_등록된_토큰을_전달하면_참을_반환한다() {
        // given
        final BlackListToken blackListToken = new BlackListToken(TokenType.ACCESS, "accessToken");

        blackListTokenRepository.save(blackListToken);

        em.flush();
        em.clear();

        // when
        final boolean actual = blackListTokenRepository
                .existsByTokenTypeAndToken(TokenType.ACCESS, "accessToken");

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 블랙리스트로_등록된_토큰인지_확인할때_블랙리스트로_등록되지_않은_토큰을_전달하면_거짓을_반환한다() {
        // given
        final String invalidAccessToken = "invalidAccessToken";

        // when
        final boolean actual = blackListTokenRepository
                .existsByTokenTypeAndToken(TokenType.ACCESS, invalidAccessToken);

        // then
        assertThat(actual).isFalse();
    }
}
