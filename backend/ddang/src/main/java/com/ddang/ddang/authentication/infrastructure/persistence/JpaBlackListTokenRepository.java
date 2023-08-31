package com.ddang.ddang.authentication.infrastructure.persistence;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    boolean existsByTokenTypeAndToken(final TokenType tokenType, final String accessToken);
}
